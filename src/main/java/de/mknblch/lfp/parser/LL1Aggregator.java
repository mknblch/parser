package de.mknblch.lfp.parser;

import de.mknblch.lfp.common.Table;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarException;
import de.mknblch.lfp.grammar.Rule;

/**
 * @author martinknobloch
 */
public class LL1Aggregator {

    private final Grammar grammar;
    private final FirstSetAggregator firstAggregator;
    private final FollowSetAggregator followAggregator;
    private Table<String, String, Rule> parseTable;

    public LL1Aggregator(Grammar grammar) {
        this.grammar = grammar;

        firstAggregator = new FirstSetAggregator(grammar);
        followAggregator = new FollowSetAggregator(grammar, firstAggregator);
    }

    public Table<String, String, Rule> getParseTable() {
        return parseTable;
    }

    public LL1Aggregator build() throws GrammarException {
        parseTable = new Table<>();
        followAggregator.aggregate();
        for (Rule rule : grammar.rules()) {
            for (String first : firstAggregator.first(rule)) {
                calc(rule, first);
            }
        }
        return this;
    }

    private void calc(Rule rule, String first) throws GrammarException {
        if (grammar.isEpsilon(first)) {
            for (String follow : followAggregator.get(rule.left)) {
                add(rule, follow);
            }
        } else {
            add(rule, first);
        }
    }

    private void add(Rule rule, String symbol) throws GrammarException {
        final Rule previous = parseTable.put(rule.left, symbol, rule);

        if (null != previous) {
            throw new GrammarException("Not LL1! Multiple rules in " + rule.left);
        }
    }
}
