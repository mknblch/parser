package de.mknblch.lfp;

import com.sun.org.apache.bcel.internal.generic.RET;

import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by mknblch on 06.12.2015.
 */
public class GrammarHelper {

    public final Map<String, List<List<String>>> ruleMap;

    private String epsilon = "EPSILON";

    private static final Set<String> EPSILON_SET = Collections.emptySet();

    public GrammarHelper(Map<String, List<List<String>>> ruleMap) {
        this.ruleMap = ruleMap;
    }

    public String getEpsilon() {
        return epsilon;
    }

    public GrammarHelper setEpsilon(String epsilon) {
        this.epsilon = epsilon;
        return this;
    }

    public Map<List<String>, Set<String>> first() throws GrammarException {
        final Map<List<String>, Set<String>> firstMap = new HashMap<>();
        for (String nonTerminal : ruleMap.keySet()) {
            final ArrayList<String> idList = new ArrayList<String>() {{
                add(nonTerminal);
            }};
            firstMap.put(idList, first(nonTerminal));

            for (List<String> rule : ruleMap.get(nonTerminal)) {
                firstMap.put(rule, first(rule));
            }
        }
        return firstMap;
    }

    private Set<String> first(String symbol) throws GrammarException {

        // make epsilon
        final HashSet<String> firsts = new HashSet<>();
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
        return symbol.startsWith(GrammarReader.TERMINAL_PREFIX);
    }
}
