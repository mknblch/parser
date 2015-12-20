package de.mknblch.lfp.parser;

import de.mknblch.lfp.common.Table;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarException;
import de.mknblch.lfp.grammar.Rule;

/**
 * @author martinknobloch
 */
public class LL1ParseTableBuilder {

    private final Grammar grammar;
    private final Aggregator aggregator;

    public LL1ParseTableBuilder(Grammar grammar) {
        this.grammar = grammar;
        aggregator = new Aggregator(grammar)
                .aggregate();
    }

    public Table<String, String, Rule> build() throws GrammarException {
        final Table<String, String, Rule> parseTable = new Table<>();
        for (Rule rule : grammar.rules()) {
            for (String first : aggregator.first(rule)) {
                if (grammar.isEpsilon(first)) {
                    for (String follow : aggregator.follow(rule.left)) {
                        addToParseTable(parseTable, rule, follow);
                    }
                } else {
                    addToParseTable(parseTable, rule, first);
                }
            }
        }
        return parseTable;
    }

    private void addToParseTable(Table<String, String, Rule> parseTable, Rule rule, String symbol) throws GrammarException {
        final Rule previous = parseTable.put(rule.left, symbol, rule);
        if (null != previous) {
            throw new GrammarException("Not LL1! Multiple rules in " + rule.left);
        }
    }
}
