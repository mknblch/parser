package de.mknblch.lfp.parser.ll1;

import de.mknblch.lfp.common.Table;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.Production;
import de.mknblch.lfp.lexer.Lexer;
import de.mknblch.lfp.lexer.SyntaxException;
import de.mknblch.lfp.lexer.Token;
import de.mknblch.lfp.lexer.TokenStream;
import de.mknblch.lfp.parser.Element;
import de.mknblch.lfp.parser.ParseException;
import de.mknblch.lfp.parser.ast.Node;
import de.mknblch.lfp.parser.ast.RuleNode;
import de.mknblch.lfp.parser.ast.ValueNode;

import java.util.List;
import java.util.Stack;

/**
 * Created by mknblch on 19.12.2015.
 */
public class Parser {

    private final Grammar grammar;
    private final Table<String, String, Production> parseTable;


    public Parser(Grammar grammar, Table<String, String, Production> parseTable) {
        this.grammar = grammar;
        this.parseTable = parseTable;
    }

    public Node parse(CharSequence input) throws ParseException, SyntaxException {
        final Stack<Element> semanticStack = new Stack<>();
        final Stack<String> stack = prepareStack();
        final TokenStream tokenStream = new Lexer(grammar).buildStream(input);
        tokenStream.next();
        do {
            final String head = stack.pop();
            final Token token = tokenStream.current();

            if (null == token) {
                throw new ParseException("Premature End at " + (tokenStream.getOffset()));
            } else if (grammar.isTerminal(head)) {
                if (!head.equals(token.identifier)) {
                    throw new ParseException("Unable to parse " + head + " at " + tokenStream.getOffset());
                }
                semanticStack.push(new Element(token));
                tokenStream.next();
                continue;
            } else {
                final Production production = parseTable.get(head, token.identifier);
                if (null == production) {
                    throw new ParseException("Unable to parse " + head + " at " + tokenStream.getOffset());
                }
                if (!grammar.isEpsilon(production)) {
                    pushReversed(stack, production.right());
                }
                semanticStack.push(new Element(production));
                continue;
            }


        } while (!stack.isEmpty());

        if (tokenStream.hasNext()) {
            throw new ParseException("Unable to parse " + tokenStream.current() + " at " + tokenStream.getOffset());

        }

        return createSyntaxTree(semanticStack);
    }

    private Node createSyntaxTree(Stack<Element> semanticStack) {
        final Stack<Node> nodes = new Stack<>();
        do {
            final Element head = semanticStack.pop();
            if (head.isRule()) {
                final RuleNode ruleNode = new RuleNode(head.production); // TODO Epsilon Rule
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
        stack.push(grammar.getStartSymbol());
        return stack;
    }

    private boolean isReducible(String head) {
        return grammar.isTerminal(head) ;
    }

    private static void pushReversed(Stack<String> stack, List<String> production) {
        for (int j = production.size() - 1; j >= 0; j--) {
            stack.push(production.get(j));
        }
    }
}
