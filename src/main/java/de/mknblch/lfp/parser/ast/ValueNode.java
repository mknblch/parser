package de.mknblch.lfp.parser.ast;

import de.mknblch.lfp.lexer.Token;

/**
 * Created by mknblch on 19.12.2015.
 */
public class ValueNode implements Node {

    private final Token value;

    public ValueNode(Token value) {
        this.value = value;
    }

    public Token getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.literal;
    }
}
