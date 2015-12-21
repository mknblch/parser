package de.mknblch.lfp.parser;

import de.mknblch.lfp.common.Table;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.Rule;
import de.mknblch.lfp.lexer.Token;

import java.util.List;
import java.util.Stack;

/**
 * Created by mknblch on 19.12.2015.
 */
public abstract class Parser implements ParserHandler {

    protected final Grammar grammar;
    protected final Table<String, String, Rule> parseTable;

    protected Parser(Grammar grammar, Table<String, String, Rule> parseTable) {
        this.grammar = grammar;
        this.parseTable = parseTable;
    }

    public void parse(List<Token> input) throws ParseException {
        onInitialize();
        final Stack<String> stack = prepareStack();
        for (int index = 0; index < input.size(); ) {
            final Token token = input.get(index);
            final String head = stack.pop();
            if (isReducible(head, token)) {
                if (!Grammar.END_SYMBOL.equals(token.identifier)) {
                    onToken(token);
                }
                index++;
                continue;
            } else if (grammar.isNonTerminal(head)) {
                final Rule rule = parseTable.get(head, token.identifier);
                if (null == rule) {
                    throw new ParseException("Unable to parse " + token + " at " + index);
                }
                if (!grammar.isEpsilon(rule)) {
                    pushReversed(stack, rule);
                }
                onRule(rule);
                continue;
            }
            throw new ParseException("Unable to parse " + token + " at " + index);
        }
        onDone();
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

    private boolean isReducible(String head, Token token) {
        return (grammar.isTerminal(head) || Grammar.END_SYMBOL.equals(head)) && token.identifier.equals(head);
    }
}
