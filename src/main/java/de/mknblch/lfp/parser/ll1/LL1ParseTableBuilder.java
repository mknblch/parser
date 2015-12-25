package de.mknblch.lfp.parser.ll1;

import de.mknblch.lfp.common.Table;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.Production;
import de.mknblch.lfp.parser.GrammarAggregator;
import de.mknblch.lfp.parser.GrammarException;

/**
 * @author martinknobloch
 */
public class LL1ParseTableBuilder {

    private final Grammar grammar;
    private final GrammarAggregator aggregator;

    public LL1ParseTableBuilder(Grammar grammar) {
        this.grammar = grammar;
        aggregator = new GrammarAggregator(grammar)
                .aggregate();
    }

    public Table<String, String, Production> build() throws GrammarException {
        final Table<String, String, Production> parseTable = new Table<>();
        for (Production production : grammar.rules()) {
            for (String first : aggregator.first(production)) {
                if (grammar.isEpsilon(first)) {
                    for (String follow : aggregator.follow(production.left())) {
                        addToParseTable(parseTable, production, follow);
                    }
                } else {
                    addToParseTable(parseTable, production, first);
                }
            }
        }
        return parseTable;
    }

    private void addToParseTable(Table<String, String, Production> parseTable, Production production, String symbol) throws GrammarException {
        final Production previous = parseTable.put(production.left(), symbol, production);
        if (null != previous) {
            throw new GrammarException("Not LL1! Multiple rules: " + " " + previous + ", " + production);
        }
    }
}
