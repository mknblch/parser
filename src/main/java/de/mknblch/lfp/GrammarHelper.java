package de.mknblch.lfp;

import com.sun.org.apache.bcel.internal.generic.RET;

import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by mknblch on 06.12.2015.
 */
public class GrammarHelper {

    private final String startSymbol;
    private final Map<String, Pattern> patternMap;
    private final Map<String, List<List<String>>> ruleMap;

    private String epsilon = "EPSILON";

    private static final Set<String> EPSILON_SET = Collections.emptySet();

    public GrammarHelper(String startSymbol,
                         Map<String, Pattern> patternMap,
                         Map<String, List<List<String>>> ruleMap) {

        this.startSymbol = startSymbol;
        this.patternMap = patternMap;
        this.ruleMap = ruleMap;
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
        for (String nonTerminal : ruleMap.keySet()) {
            firstMap.put(nonTerminal, first(nonTerminal));
        }
        for (String terminal : patternMap.keySet()) {
            firstMap.put(terminal, first(terminal));
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

        final List<List<String>> rules = ruleMap.get(symbol);
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
        if (symbols.size() != 1) {
            return false;
        }
        return symbols.iterator().next().equals(epsilon);
    }

    private boolean isTerminal(String symbol) {
        return symbol.startsWith(GrammarReader.TERMINAL_PREFIX) || symbol.startsWith(GrammarReader.EXCLUDE_PREFIX);
    }

    public Map<String, Set<String>> follow(String startSymbol) {

        final HashMap<String, Set<String>> ret = new HashMap<>();


        for (String symbol : ruleMap.keySet()) {
            // Start symbol
            if (startSymbol.equals(symbol)) {
                ret.put(startSymbol, toSet("$"));
                continue;
            }

            final Set<String> follows = ruleMap.values().parallelStream()
                    .flatMap(Collection::stream)
                    .map(rule -> follow(symbol, rule))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());

            ret.put(symbol, follows);
        }

        return ret;
    }

    private Set<String> follow(String symbol, List<String> rule) {

        final int firstIndexOfSymbol = rule.indexOf(symbol);

        if (firstIndexOfSymbol == -1) {
            return Collections.emptySet();
        }



        return null;
    }
}
