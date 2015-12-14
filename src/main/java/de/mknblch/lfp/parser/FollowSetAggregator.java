package de.mknblch.lfp.parser;

import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarException;
import de.mknblch.lfp.grammar.Rule;

import java.util.*;

/**
 * Created by mknblch on 13.12.2015.
 */
public class FollowSetAggregator {

    private final Grammar grammar;
    private final Map<String, Set<String>> firstMap;

    private final Map<String, Set<String>> followSet = new HashMap<>();

    public FollowSetAggregator(Grammar grammar, Map<String, Set<String>> firstMap) {
        this.grammar = grammar;
        this.firstMap = firstMap;
    }


    private Set<String> toSet(String symbol) {
        return new HashSet<String>() {{
            add(symbol);
        }};
    }

    private boolean isEpsilonRule(Rule rule) {

        return rule.allEquals(grammar.epsilonSymbol);
    }

    private void balance() {
        for (Map.Entry<String, Set<String>> entry : followSet.entrySet()) {

           entry.getValue().stream()
                   .filter(grammar::isNonTerminal)
                   .peek(entry.getValue()::remove)
                   .forEach(nt -> {
                       try {
                           firstMap.put(entry.getKey(), follow(nt));
                       } catch (GrammarException e) {
                           e.printStackTrace();
                       }
                   });
        }
    }

    public Map<String, Set<String>> follow() throws GrammarException {

        for (final String find : grammar.ruleMap.keySet()) {
            // Start symbol
            if (grammar.startSymbol.equals(find)) {
                followSet.put(grammar.startSymbol, toSet("$"));
                continue;
            }
            followSet.put(find, follow(find));
        }

        balance();
        return followSet;
    }

    private HashSet<String> follow(String find) throws GrammarException {
        final HashSet<String> follows = new HashSet<>();
        for (List<Rule> rules : grammar.ruleMap.values()) {
            for (Rule rule : rules) {

                if (rule.left.equals(find)) {
                    continue;
                }

                follows.addAll(follow(find, rule));
            }
        }
        return follows;
    }

    private Set<String> follow(String find, Rule rule) throws GrammarException {
        final List<Integer> indices = rule.find(find);
        final HashSet<String> ret = new HashSet<>();
        for (Integer index : indices) {
            ret.addAll(follow(rule, index));
        }
        return ret;
    }

    private Set<String> follow(Rule rule, int index) throws GrammarException {

        final HashSet<String> set = new HashSet<>();

        for (int i = index + 1; i < rule.size(); i++) {

            final String symbol = rule.get(index);

            final Set<String> first = firstMap.get(symbol);

            set.addAll(first);
            set.remove(grammar.epsilonSymbol);

            if (!first.contains(grammar.epsilonSymbol)) {
                return set;
            }
        }

        set.add(rule.left);

        return set;
    }

}
