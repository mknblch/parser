package de.mknblch.lfp.parser;

import de.mknblch.lfp.grammar.Grammar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mknblch on 09.12.2015.
 */
public class DFAHelper {

    private final Grammar grammar;

    private final Map<String, State> states = new HashMap<>();

    public DFAHelper(Grammar grammar) {
        this.grammar = grammar;
    }

    private void preProcess() {

        for (String symbol : grammar.ruleMap.keySet()) {
            states.put(symbol, new State(symbol));
        }

        for (Map.Entry<String, State> entry : states.entrySet()) {
            final List<List<String>> prodRules = grammar.ruleMap.get(entry.getKey());
            for (List<String> rule : prodRules) {
                processRule(entry.getValue(), rule);
            }
        }
    }

    private void processRule(State state, List<String> rule) {


    }
}
