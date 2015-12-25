package de.mknblch.lfp.lexer;

import de.mknblch.lfp.grammar.Grammar;

/**
 * Created by mknblch on 05.12.2015.
 */
public class Lexer {

    private Grammar grammar;
    private CharSequence input;

    public Lexer setGrammar(Grammar grammar) {
        this.grammar = grammar;
        return this;
    }

    public Lexer setInput(CharSequence charSequence) {
        this.input = charSequence;
        return this;
    }

    public TokenStream build() {
        return new TokenStream(input, grammar.getPatternMap(), grammar.getExclusionMap());
    }
}
