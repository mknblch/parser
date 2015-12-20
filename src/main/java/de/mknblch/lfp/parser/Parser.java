package de.mknblch.lfp.parser;

import de.mknblch.lfp.common.Table;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarException;
import de.mknblch.lfp.grammar.Rule;
import de.mknblch.lfp.lexer.Lexer;
import de.mknblch.lfp.lexer.LexerException;
import de.mknblch.lfp.lexer.Token;

import java.util.List;
import java.util.Stack;

/**
 * Created by mknblch on 19.12.2015.
 */
public class Parser {

    private final Grammar grammar;
    private Table<String, String, Rule> parseTable;

    public Parser(Grammar grammar) throws GrammarException {
        this.grammar = grammar;
        this.parseTable = new LL1ParseTableBuilder(grammar).build();
    }

    public Node parse(String input) throws ParseException, LexerException, GrammarException {
        parseTable = new LL1ParseTableBuilder(grammar).build();
        System.out.println(parseTable);
        return parse(new Lexer(grammar).tokenize(input));
    }

    private boolean isReducible(String head, Token token) {
        return (grammar.isTerminal(head) || Grammar.END_SYMBOL.equals(head)) && token.identifier.equals(head);
    }

    private Node parse(List<Token> tokens) throws ParseException {
        final Stack<String> stack = prepareStack();
        tokens.add(new Token(Grammar.END_SYMBOL, Grammar.END_SYMBOL));
        final NodeBuilder nodeBuilder = new NodeBuilder(grammar.getStartSymbol());

        for (int index = 0; index < tokens.size(); ) {
            final Token token = tokens.get(index);
            final String head = stack.pop();
            if (isReducible(head, token)) {
                index++;
//                System.out.println("reducing " + head);
                nodeBuilder.reduce(head);
                continue;
            } else if (grammar.isNonTerminal(head)) {
                final Rule rule = parseTable.get(head, token.identifier);
                if (null == rule) {
                    throw new ParseException("Unable to parse " + token + " at " + index);
                }
                if (!grammar.isEpsilon(rule)) {
                    pushReversed(stack, rule);
                }
                nodeBuilder.reduce(rule);
//                System.out.println("Substituting from " + lastSymbol + " -> " + rule);
                continue;
            }
            throw new ParseException("Unable to parse " + token + " at " + index);
        }

        return null;
    }

    private void pushReversed(Stack<String> stack, Rule rule) {
        for (int j = rule.size() - 1; j >= 0; j--) {
            stack.push(rule.get(j));
        }
    }


    private Stack<String> prepareStack() {
        final Stack<String> stack = new Stack<>();
        stack.clear();
        stack.push(Grammar.END_SYMBOL);
        stack.push(grammar.getStartSymbol());
        return stack;
    }
}
