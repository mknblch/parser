package de.mknblch.lfp.lexer;

/**
 * Created by mknblch on 05.12.2015.
 */
public class Token {

    public final String identifier;
    public final String literal;

    Token(String identifier, String literal) {
        this.identifier = identifier;
        this.literal = literal;
    }

    @Override
    public String toString() {

        return identifier + ":" + literal;
    }
}
