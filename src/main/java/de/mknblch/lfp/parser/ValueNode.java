package de.mknblch.lfp.parser;

import de.mknblch.lfp.lexer.Token;

/**
 * Created by mknblch on 19.12.2015.
 */
public class ValueNode implements Node {

    private final String symbol;
    private final Token value;

    public ValueNode(String symbol, Token value) {
        this.symbol = symbol;
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public Token getValue() {
        return value;
    }
}
