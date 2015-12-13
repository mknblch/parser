package de.mknblch.lfp.parser;

import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarException;

import java.util.Map;
import java.util.Set;

/**
 * @author martinknobloch
 */
public class ParseTableCalc {

    private final Grammar grammar;
    private final Map<String, Set<String>> firstSet;
    private final Map<String, Set<String>> followSet;

    public ParseTableCalc(Grammar grammar) throws GrammarException {
        this.grammar = grammar;

        final FirstFollowCalc calc = new FirstFollowCalc(grammar);
        firstSet = calc.first();
        followSet = calc.follow();
    }

//    public ParseTable build() {
//
//        for (String nonTerminal : grammar.nonTerminals()) {
//
//
//        }
//    }
}
