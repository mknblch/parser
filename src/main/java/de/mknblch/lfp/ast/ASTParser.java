package de.mknblch.lfp.ast;

import de.mknblch.lfp.common.Table;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.Rule;
import de.mknblch.lfp.lexer.Token;
import de.mknblch.lfp.parser.ll1.Parser;

import java.util.Stack;

/**
 * Created by mknblch on 21.12.2015.
 */
public class ASTParser extends Parser {

    private Node ast;

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

    private final Stack<Element> semanticStack = new Stack<>();

    public Node getAst() {
        return ast;
    }

    @Override
    public void onInitialize() {
        semanticStack.clear();
    }

    @Override
    public void onToken(Token token) {
        semanticStack.push(new Element(token));
    }

    @Override
    public void onRule(Rule rule) {
        semanticStack.push(new Element(rule));
    }

    @Override
    public void onDone() {
        process();
    }

    private void process() {
        final Stack<Node> nodes = new Stack<>();
        do {
            final Element head = semanticStack.pop();
            if (head.isRule()) {
                final RuleNode ruleNode = new RuleNode(head.rule); // TODO Epsilon Rule
                if (!grammar.isEpsilon(ruleNode.getRule())) {
                    while (!ruleNode.isSatisfied()) {
                        ruleNode.addChild(nodes.pop());
                    }
                }
                nodes.push(ruleNode);
            } else {
                nodes.push(new ValueNode(head.token));
            }

        } while (!semanticStack.isEmpty());
        ast = nodes.pop();
    }

    private void dump(Stack stack) {

        for (Object element : stack) {
            System.out.println(element);
        }
    }
}
