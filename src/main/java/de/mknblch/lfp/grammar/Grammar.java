package de.mknblch.lfp.grammar;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by mknblch on 05.12.2015.
 */
public class Grammar {

    public final String startSymbol;
    public final String epsilonSymbol;
    public final Map<String, Pattern> exclusionMap;
    public final Map<String, Pattern> patternMap;
    public final Map<String, List<Rule>> ruleMap;

    public Grammar(String startSymbol,
                   String epsilonSymbol, Map<String, Pattern> exclusionMap, Map<String, Pattern> patternMap,
                   Map<String, List<Rule>> ruleMap) {

        this.startSymbol = startSymbol;
        this.epsilonSymbol = epsilonSymbol;
        this.exclusionMap = exclusionMap;
        this.patternMap = patternMap;
        this.ruleMap = ruleMap;
    }

    public boolean isNullable(String symbol) {
        return ruleMap.get(symbol).stream().anyMatch(this::isNullable);
    }

    public boolean isNullable(Rule rule) {
        return rule.size() == 1 && rule.allEquals(epsilonSymbol);
    }

    public boolean isEpsilon(String symbol) {
        return epsilonSymbol.equals(symbol);
    }

    public boolean isTerminal(String symbol) {
        return patternMap.containsKey(symbol);
    }

    public boolean isNonTerminal(String symbol) {
        return ruleMap.containsKey(symbol);
    }

    public Set<String> terminals() {
        return patternMap.keySet();
    }

    public Set<String> nonTerminals() {
        return ruleMap.keySet();
    }

    public Set<String> nullable() {
        return ruleMap.keySet()
                .stream()
                .filter(this::isNullable)
                .collect(Collectors.toSet());
    }

    public boolean isSymbol(String symbol) {
        return isTerminal(symbol) || isNonTerminal(symbol);
    }

    public List<Rule> rules() {
        return ruleMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();

        buffer.append("OPTIONS:").append("\n");

        buffer.append("\tSTART -> ").append(startSymbol).append("\n");
        buffer.append("\tEPSILON -> ").append(epsilonSymbol).append("\n");

        buffer.append("EXCLUDES:\n");
        for (String symbol : exclusionMap.keySet()) {
            buffer.append("\t")
                    .append(symbol)
                    .append(" -> ")
                    .append(exclusionMap.get(symbol))
                    .append("\n");
        }
        buffer.append("TERMINALS:\n");
        for (String terminal : patternMap.keySet()) {
            buffer.append("\t")
                    .append(terminal)
                    .append(" -> ")
                    .append(patternMap.get(terminal))
                    .append("\n");
        }
        buffer.append("RULES:\n");
        for (String nonTerminal : ruleMap.keySet()) {
            buffer.append("\t")
                    .append(nonTerminal)
                    .append(" -> ")
                    .append(getRuleDump(nonTerminal))
                    .append("\n");
        }

        return buffer.toString();
    }

    private String getRuleDump(String nonTerminal) {
        final List<Rule> rules = ruleMap.get(nonTerminal);
        final StringBuilder buffer = new StringBuilder();

        for (Rule rule : rules) {
            if (buffer.length() != 0) {
                buffer.append(" | ");
            }
            buffer.append(rule);
        }
        return buffer.toString();
    }
}