package de.mknblch.lfp.lexer;

import de.mknblch.lfp.grammar.Grammar;

/**
 * Created by mknblch on 05.12.2015.
 */
public class Lexer {

    private final Grammar grammar;

    public Lexer(Grammar grammar) {
        this.grammar = grammar;
    }

    public TokenStream buildStream(CharSequence input) {
        return new TokenStream(grammar, input);
    }
}
