package de.mknblch.lfp;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by mknblch on 05.12.2015.
 */
public class Grammar {

    public final String startSymbol;
    public final Map<String, Pattern> patternMap;
    public final Map<String, List<List<String>>> ruleMap;
    public final Map<List<String>, Set<String>> firstSet;
    public final Map<String, Set<String>> followSet;

    public Grammar(String startSymbol, Map<String, Pattern> patternMap, Map<String, List<List<String>>> ruleMap, Map<List<String>, Set<String>> firstSet, Map<String, Set<String>> followSet) {
        this.startSymbol = startSymbol;
        this.patternMap = patternMap;
        this.ruleMap = ruleMap;
        this.firstSet = firstSet;
        this.followSet = followSet;
    }

    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("START SYMBOL: ").append(startSymbol).append("\n");

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

        buffer.append("FIRST:\n");
        for (List<String> rule : firstSet.keySet()) {
            buffer.append("\t")
                    .append(collectionToString(rule))
                    .append(" -> ")
                    .append(firstSet.get(rule))
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