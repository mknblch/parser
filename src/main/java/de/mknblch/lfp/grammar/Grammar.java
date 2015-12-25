package de.mknblch.lfp.grammar;

import de.mknblch.lfp.common.DumpHelper;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by mknblch on 05.12.2015.
 */
public class Grammar {

    public static final String END_SYMBOL = "$";

    private final String startSymbol;
    private final String epsilonSymbol;
    private final Map<String, Pattern> exclusionMap;
    private final Map<String, Pattern> patternMap;
    private final Map<String, List<Production>> ruleMap;

    public Grammar(String startSymbol,
                   String epsilonSymbol, Map<String, Pattern> exclusionMap, Map<String, Pattern> patternMap,
                   Map<String, List<Production>> ruleMap) {

        this.startSymbol = startSymbol;
        this.epsilonSymbol = epsilonSymbol;
        this.exclusionMap = exclusionMap;
        this.patternMap = patternMap;
        this.ruleMap = ruleMap;
    }

    public boolean isEpsilon(String symbol) {
        return epsilonSymbol.equals(symbol);
    }

    public boolean isEpsilon(Production production) {
        return production.size() == 1 && epsilonSymbol.equals(production.get(0));
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


    public boolean isSymbol(String symbol) {
        return END_SYMBOL.equals(symbol) || isTerminal(symbol) || isNonTerminal(symbol);
    }

    public List<Production> rules() {
        return ruleMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public String getEpsilonSymbol() {
        return epsilonSymbol;
    }

    public Map<String, Pattern> getExclusionMap() {
        return exclusionMap;
    }

    public Map<String, Pattern> getPatternMap() {
        return patternMap;
    }

    public Map<String, List<Production>> getRuleMap() {
        return ruleMap;
    }

    @Override
    public String toString() {
        return DumpHelper.getGrammarDump(this);
    }
}