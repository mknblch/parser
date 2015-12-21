package de.mknblch.lfp.ast;

import de.mknblch.lfp.grammar.Rule;
import de.mknblch.lfp.lexer.Token;

import java.util.Stack;

/**
 * Created by mknblch on 19.12.2015.
 */
public class NodeBuilder {

    private static final class Element {

        public final Rule rule;
        public final Token token;

        private Element(Rule rule) {
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

    private final Stack<Element> queue;
    private final String startSymbol;
    private Node currentNode;


    public NodeBuilder(String startSymbol) {

        this.startSymbol = startSymbol;
        queue = new Stack<>();
    }

    public void build() {

    }


    public void reduce(Token token) {
        queue.push(new Element(token));
    }

    public void reduce(Rule rule) {
        queue.push(new Element(rule));
    }


    public void dump() {

        for (int i = queue.size() - 1; i >= 0; i--) {
            System.out.println(queue.get(i));
        }
    }
}
