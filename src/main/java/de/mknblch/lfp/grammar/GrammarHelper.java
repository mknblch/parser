package de.mknblch.lfp.grammar;

import java.util.*;

/**
 * Created by mknblch on 06.12.2015.
 */
public class GrammarHelper {

    private final Grammar grammar;
    private String epsilon = "E";


    public GrammarHelper(Grammar grammar) {
        this.grammar = grammar;
    }

    public String getEpsilon() {
        return epsilon;
    }

    public GrammarHelper setEpsilon(String epsilon) {
        this.epsilon = epsilon;
        return this;
    }

    public Map<String, Set<String>> first() throws GrammarException {
        final Map<String, Set<String>> firstMap = new HashMap<>();
        for (String nonTerminal : grammar.ruleMap.keySet()) {
            firstMap.put(nonTerminal, first(nonTerminal));
        }
        for (String terminal : grammar.patternMap.keySet()) {
            firstMap.put(terminal, toSet(terminal));
        }
        return firstMap;
    }

    private Set<String> first(String symbol) throws GrammarException {

        // make epsilon
        final HashSet<String> firsts = new HashSet<>();

        if (isTerminal(symbol)) {
            firsts.add(symbol);
            return firsts;
        }

        if (epsilon.equals(symbol)) {
            firsts.add(symbol);
            return firsts;
        }

        final List<List<String>> rules = grammar.ruleMap.get(symbol);
        // iterate each rule
        for (List<String> rule : rules) {
            firsts.addAll(first(rule));
        }
        return firsts;
    }

    private Set<String> first(List<String> rule) throws GrammarException {

        final Set<String> ret = new HashSet<>();
        if (onlyEpsilon(rule)) {
            ret.add(epsilon);
            return ret;
        }

        for (final String symbol : rule) {

            if (isTerminal(symbol)) {
                ret.add(symbol);
                return ret;
            }

            final Set<String> first = first(symbol);

            if (first.contains(epsilon)) {
                first.remove(epsilon);
                ret.addAll(first);
                continue;
            }
            return first;
        }

        ret.add(epsilon);
        return ret;
    }

    private Set<String> toSet(String symbol) {
        return new HashSet<String>() {{
                add(symbol);
            }};
    }

    private boolean onlyEpsilon(Collection<String> symbols) {
        for (String symbol : symbols) {
            if (!epsilon.equals(symbol)) {
                return false;
            }
        }
        return true;
    }

    private boolean isTerminal(String symbol) {
        return symbol.startsWith(GrammarReader.TERMINAL_PREFIX) ;
    }

    public Map<String, Set<String>> follow() throws GrammarException {

        final HashMap<String, Set<String>> ret = new HashMap<>();
        for (String symbol : grammar.ruleMap.keySet()) {
            // Start symbol
            if (grammar.startSymbol.equals(symbol)) {
                ret.put(grammar.startSymbol, toSet("$"));
                continue;
            }
            final HashSet<String> follows = new HashSet<>();
            for (List<List<String>> rules : grammar.ruleMap.values()) {
                for (List<String> rule : rules) {
                    follows.addAll(follow(symbol, rule));
                }
            }
            ret.put(symbol, follows);
        }
        return ret;
    }

    private Set<String> follow(String symbol, List<String> rule) throws GrammarException {

        final int firstIndexOfSymbol = rule.indexOf(symbol);

        if (firstIndexOfSymbol == -1) {
            return Collections.emptySet();
        }

        if (firstIndexOfSymbol+1 >= rule.size()) {
            return toSet(epsilon);
        }

        return first(rule.get(firstIndexOfSymbol + 1));
    }
}