package de.mknblch.lfp.parser;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by mknblch on 08.12.2015.
 */
public class State {

    final String symbol;
    private final Map<String, State> transitions;

    public State(String symbol) {
        this.symbol = symbol;
        this.transitions = new HashMap<>();
    }

    void addTransition(String symbol, State state) {
        transitions.put(symbol, state);
    }

    public State transit(String input) throws ParseException {
        final State state = transitions.get(input);
        if (null == state) {
            throw new ParseException("Unable to parse " + input);
        }
        return state;
    }
}
