package de.mknblch.lfp.grammar;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by mknblch on 05.12.2015.
 */
public class Grammar {

    public final String startSymbol;
    public final Map<String, Pattern> exclusionMap;
    public final Map<String, Pattern> patternMap;
    public final Map<String, List<List<String>>> ruleMap;

    public Grammar(String startSymbol,
                   Map<String, Pattern> exclusionMap, Map<String, Pattern> patternMap,
                   Map<String, List<List<String>>> ruleMap) {

        this.startSymbol = startSymbol;
        this.exclusionMap = exclusionMap;
        this.patternMap = patternMap;
        this.ruleMap = ruleMap;
    }

    public boolean isTerminal(String symbol) {
        return patternMap.containsKey(symbol);
    }

    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("START SYMBOL: ").append(startSymbol).append("\n");

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

    private String collectionToString(Collection<String> list) {
        final StringBuilder buffer = new StringBuilder();
        for (String symbol : list) {
            if (buffer.length() != 0) {
                buffer.append(" ");
            }
            buffer.append(symbol);
        }
        return buffer.toString();
    }

    private String getRuleDump(String nonTerminal) {
        final List<List<String>> rules = ruleMap.get(nonTerminal);
        final StringBuilder buffer = new StringBuilder();

        for (List<String> rule : rules) {
            if (buffer.length() != 0) {
                buffer.append(" | ");
            }
            buffer.append(collectionToString(rule));
        }
        return buffer.toString();
    }
}