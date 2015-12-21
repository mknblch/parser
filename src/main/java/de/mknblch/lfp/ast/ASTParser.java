package de.mknblch.lfp.ast;

import de.mknblch.lfp.common.Table;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.Rule;
import de.mknblch.lfp.lexer.Token;
import de.mknblch.lfp.parser.Parser;

import java.util.Stack;

/**
 * Created by mknblch on 21.12.2015.
 */
public class ASTParser extends Parser {

    public ASTParser(Grammar grammar, Table<String, String, Rule> parseTable) {
        super(grammar, parseTable);
    }

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

    private Stack<Element> queue;

    @Override
    public void onInitialize() {
        queue = new Stack<>();
    }

    @Override
    public void onToken(Token token) {
        queue.push(new Element(token));
    }

    @Override
    public void onRule(Rule rule) {
        queue.push(new Element(rule));
    }

    @Override
    public void onDone() {

        dump(queue);

        final Stack<Node> nodes = new Stack<>();

        do {

            final Element pop = queue.pop();

            if (pop.isRule()) {

                final RuleNode item = new RuleNode(pop.rule);

                while (!item.isSatisfied()) {
                    item.addChild(nodes.pop());
                }


                nodes.push(item);
            } else {
                nodes.push(new ValueNode(pop.token));
            }

        } while (!queue.isEmpty());

        dump(nodes);
    }

    private void dump(Stack stack) {

        for (Object element : stack) {
            System.out.println(element);
        }
    }
}
