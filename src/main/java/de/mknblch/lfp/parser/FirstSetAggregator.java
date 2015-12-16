package de.mknblch.lfp.parser;

import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarException;
import de.mknblch.lfp.grammar.Rule;

import java.util.*;

/**
 * Created by mknblch on 13.12.2015.
 */
public class FirstSetAggregator {

    private final Grammar grammar;

    private final Map<String, Set<String>> firstSet = new HashMap<>();

    public FirstSetAggregator(Grammar grammar) {
        this.grammar = grammar;
    }

    public Map<String, Set<String>> getFirstSets() throws GrammarException {
        for (String nonTerminal : grammar.ruleMap.keySet()) {
            firstSet.put(nonTerminal, first(nonTerminal));
        }

        return firstSet;
    }

    public Set<String> first(String symbol) throws GrammarException {

//        final Set<String> previous = firstSet.get(symbol);
//        if (null != previous) {
//            return previous;
//        }

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

    private boolean isEpsilonRule(Rule rule) {
        return rule.allEquals(grammar.epsilonSymbol);
    }
}
