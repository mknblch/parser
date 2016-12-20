package de.mknblch.lfp.parser;

import de.mknblch.lfp.grammar.Production;
import de.mknblch.lfp.lexer.Token;

public class Element {

    public final Production production;
    public final Token token;

    public Element(Production production) {
        this.production = production;
        token = null;
    }

    public Element(Token token) {
        this.token = token;
        production = null;
    }

    public boolean isRule() {
        return production != null;
    }

    @Override
    public String toString() {
        if (null != production) {
            return production.toString();
        } else if (null != token) {
            return token.toString();
        }
        return null;
    }
}