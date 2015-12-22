package de.mknblch.lfp.parser;

import de.mknblch.lfp.grammar.Rule;
import de.mknblch.lfp.lexer.Token;

public class Element {

    public final Rule rule;
    public final Token token;

    public Element(Rule rule) {
        this.rule = rule;
        token = null;
    }

    public Element(Token token) {
        this.token = token;
        rule = null;
    }

    public boolean isRule() {
        return rule != null;
    }

    @Override
    public String toString() {
        if (null != rule) {
            return rule.toString();
        } else if (null != token) {
            return token.toString();
        }
        return null;
    }
}