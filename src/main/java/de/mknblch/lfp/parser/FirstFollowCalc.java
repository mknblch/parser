package de.mknblch.lfp.parser;

import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarException;
import de.mknblch.lfp.grammar.Rule;

import java.util.*;

/**
 * Created by mknblch on 06.12.2015.
 */
public class FirstFollowCalc {

    private final Grammar grammar;


    public FirstFollowCalc(Grammar grammar) {
        this.grammar = grammar;
    }

    public Map<String, Set<String>> first() throws GrammarException {
        final Map<String, Set<String>> firstMap = new HashMap<>();
        for (String nonTerminal : grammar.ruleMap.keySet()) {
            firstMap.put(nonTerminal, first(nonTerminal));
        }

        return firstMap;
    }

    private Set<String> first(String symbol) throws GrammarException {

        final HashSet<String> firstSet = new HashSet<>();

        if (grammar.isTerminal(symbol)) {
            firstSet.add(symbol);
            return firstSet;
        }

        if (grammar.isEpsilon(symbol)) {
            firstSet.add(symbol);
            return firstSet;
        }

        final List<Rule> rules = grammar.ruleMap.get(symbol);
        // iterate each rule
        for (Rule rule : rules) {
            firstSet.addAll(first(rule));
        }
        return firstSet;
    }

    private Set<String> first(Rule rule) throws GrammarException {
        return first(rule, 0);
    }

    private Set<String> first(Rule rule, int index) throws GrammarException {

        final Set<String> ret = new HashSet<>();
        if (isEpsilonRule(rule)) {
            ret.add(grammar.epsilonSymbol);
            return ret;
        }

        if (index >= rule.size()) {
            return ret;
        }

        final String symbol = rule.get(index);
        if (grammar.isTerminal(symbol)) {
            ret.add(symbol);
            return ret;
        }

        final Set<String> first = first(symbol);

        first.stream()
                .filter(s -> !grammar.isEpsilon(symbol))
                .forEach(ret::add);

        if (first.contains(grammar.epsilonSymbol)) {
            ret.addAll(first(rule, index + 1));
        }

        return ret;
    }

    private Set<String> toSet(String symbol) {
        return new HashSet<String>() {{
            add(symbol);
        }};
    }

    private boolean isEpsilonRule(Rule rule) {

        return rule.allEquals(grammar.epsilonSymbol);
    }

    public Map<String, Set<String>> follow() throws GrammarException {

        final HashMap<String, Set<String>> followSet = new HashMap<>();
        for (final String find : grammar.ruleMap.keySet()) {
            // Start symbol
            if (grammar.startSymbol.equals(find)) {
                followSet.put(grammar.startSymbol, toSet("$"));
                continue;
            }
            final HashSet<String> follows = new HashSet<>();
            for (List<Rule> rules : grammar.ruleMap.values()) {
                for (Rule rule : rules) {
                    follows.addAll(follow(find, rule));
                }
            }
            followSet.put(find, follows);


        }
        return followSet;
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

        // symbol found at end
        if (index == rule.size() - 1) {
            return toSet("$");
        }



        for (int i = index + 1; i < rule.size(); i++) {

            final Set<String> first = first(rule, i);

            set.addAll(first);
            set.remove(grammar.epsilonSymbol);

            if (!first.contains(grammar.epsilonSymbol)) {
                return set;
            }
        }

        set.add("$");

        return set;
    }

}
