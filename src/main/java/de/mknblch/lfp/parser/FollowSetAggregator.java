package de.mknblch.lfp.parser;

import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarException;
import de.mknblch.lfp.grammar.Rule;

import java.util.*;
import java.util.function.Predicate;
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
        followSet.put(nonTerminal, symbols);
    }

    private void add(String nonTerminal, Set<String> symbols) {
        followSet.put(nonTerminal, symbols);
    }

    public Bag<String, String> follow() throws GrammarException {
        return followSet;
    }

    private void build() {
        grammar.rules().forEach(this::follow);
        while(reduce());
    }

    private boolean reduce() {

        boolean changed = false;
        for (Map.Entry<String, Set<String>> entry : followSet.entrySet()) {
            final String nonTerminal = entry.getKey();
            final Set<String> follows = entry.getValue();
            changed |= followSet.replaceIf(nonTerminal::equals, follows);
        }

        return changed;
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

    private void follow(Rule rule) {

        final Set<String> nonTerminals = grammar.nonTerminals();

        for (String nonTerminal : nonTerminals) {

            rule.find(nonTerminal)
                    .forEach(index -> follow(nonTerminal, rule, index));
        }

    }

    private void follow(String nonTerminal, Rule rule, int index) {
        for (int i = index + 1; i < rule.size(); i++) {
            final String symbol = rule.get(i);
            final Set<String> first = first(symbol);
            if (!first.contains(grammar.epsilonSymbol)) {
                add(nonTerminal, first);
                return;
            }
            first.remove(grammar.epsilonSymbol);
            add(nonTerminal, first);
        }
        add(nonTerminal, rule.left);
    }

}
