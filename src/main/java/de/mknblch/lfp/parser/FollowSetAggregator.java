package de.mknblch.lfp.parser;

import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarException;
import de.mknblch.lfp.grammar.Rule;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mknblch on 13.12.2015.
 */
public class FollowSetAggregator {

    private final Grammar grammar;
    private final Map<String, Set<String>> firstMap;

    private final Bag<String, String> followSet = new Bag<>();

    public FollowSetAggregator(Grammar grammar, Map<String, Set<String>> firstMap) {
        this.grammar = grammar;
        this.firstMap = firstMap;

        add(grammar.startSymbol, "$");
        build();
    }

    private void add(String nonTerminal, String... symbols) {
        final HashSet<String> set = new HashSet<>();
        Collections.addAll(set, symbols);
        add(nonTerminal, set);
    }

    private void add(String nonTerminal, Set<String> symbols) {
        Set<String> follows = followSet.get(nonTerminal);
        if (null == follows) {
            follows = new HashSet<>();
            followSet.put(nonTerminal, follows);
        }
        follows.addAll(symbols);
    }

    public Bag<String, String> follow() throws GrammarException {
        return followSet;
    }

    private void build() {
        grammar.rules().forEach(this::followRule);
        reduce();
    }

    private Set<String> reduce(String left, String nonTerminal) {
        final Set<String> ret = first(nonTerminal);
        if (ret.contains(grammar.epsilonSymbol)) {
            ret.remove(grammar.epsilonSymbol);
            ret.addAll(followSet.get(left));
        }
        return ret;
    }

    private void reduce() {

        final HashMap<String, Set<String>> append = new HashMap<>();
        do {
            append.clear();
            for (Map.Entry<String, Set<String>> entry : followSet.entrySet()) {
                final Set<String> follows = entry.getValue().stream()
                        .filter(grammar::isNonTerminal)
                        .map(s -> reduce(entry.getKey(), s)) // error
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());

                append.put(entry.getKey(), follows);
            }



            append.entrySet().stream().forEach(e -> followSet.get(e.getKey()).addAll(e.getValue()));

        } while (!append.isEmpty());

    }

    private void followRule(Rule rule) {
        for (int index = 0; index < rule.size(); index++) {
            final String symbol = rule.get(index);
            if (!grammar.isNonTerminal(symbol)) {
                continue;
            }
            if (index + 1 >= rule.size()) {
                add(symbol, rule.left);
            } else {
                add(symbol, rule.get(index+1));
            }
        }
    }

    private Set<String> follow(Rule rule, int index) throws GrammarException {

        final HashSet<String> set = new HashSet<>();

        for (int i = index + 1; i < rule.size(); i++) {

            final String symbol = rule.get(index);

            final Set<String> first = first(symbol);

            set.addAll(first);
            set.remove(grammar.epsilonSymbol);

            if (!first.contains(grammar.epsilonSymbol)) {
                return set;
            }
        }

        set.add(rule.left);

        return set;
    }

    private Set<String> toSet(String symbol) {
        return new HashSet<String>() {{
            add(symbol);
        }};
    }


    private Set<String> first(String symbol) {
        if (grammar.isTerminal(symbol)) {
            return toSet(symbol);
        }
        return firstMap.get(symbol);
    }

}
