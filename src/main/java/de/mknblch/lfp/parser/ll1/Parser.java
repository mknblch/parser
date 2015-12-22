package de.mknblch.lfp.parser.ll1;

import de.mknblch.lfp.ast.Node;
import de.mknblch.lfp.ast.RuleNode;
import de.mknblch.lfp.ast.ValueNode;
import de.mknblch.lfp.common.Table;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.Rule;
import de.mknblch.lfp.lexer.Token;
import de.mknblch.lfp.parser.Element;
import de.mknblch.lfp.parser.ParseException;

import java.util.List;
import java.util.Stack;

/**
 * Created by mknblch on 19.12.2015.
 */
public class Parser {

    private final Grammar grammar;
    private final Table<String, String, Rule> parseTable;


    public Parser(Grammar grammar, Table<String, String, Rule> parseTable) {
        this.grammar = grammar;
        this.parseTable = parseTable;
    }

    public Node parse(List<Token> input) throws ParseException {
        final Stack<Element> semanticStack = new Stack<>();
        final Stack<String> stack = prepareStack();
        for (int index = 0; index < input.size(); ) {
            final Token token = input.get(index);
            final String head = stack.pop();
            if (isReducible(head, token)) {
                if (!Grammar.END_SYMBOL.equals(token.identifier)) {
                    semanticStack.push(new Element(token));
                }
                index++;
                continue;
            } else if (grammar.isNonTerminal(head)) {
                final Rule rule = parseTable.get(head, token.identifier);
                if (null == rule) {
                    throw new ParseException("Unable to parse " + token + " at " + index);
                }
                if (!grammar.isEpsilon(rule)) {
                    pushReversed(stack, rule.right());
                }
                semanticStack.push(new Element(rule));
                continue;
            }
            throw new ParseException("Unable to parse " + token + " at " + index);
        }
        return createSyntaxTree(semanticStack);
    }

    private Node createSyntaxTree(Stack<Element> semanticStack) {
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
        return nodes.pop();
    }

    private Stack<String> prepareStack() {
        final Stack<String> stack = new Stack<>();
        stack.clear();
        stack.push(Grammar.END_SYMBOL);
        stack.push(grammar.getStartSymbol());
        return stack;
    }

    private boolean isReducible(String head, Token token) {
        return (grammar.isTerminal(head) || Grammar.END_SYMBOL.equals(head)) && token.identifier.equals(head);
    }

    private static void pushReversed(Stack<String> stack, List<String> production) {
        for (int j = production.size() - 1; j >= 0; j--) {
            stack.push(production.get(j));
        }
    }
}
