package de.mknblch.lfp.parser;

import de.mknblch.lfp.common.Bag;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.Rule;

import java.util.*;

/**
 * Created by mknblch on 13.12.2015.
 */
public class FollowSetAggregator {

    private final Grammar grammar;
    private final Map<String, Set<String>> firstMap;
    private final Bag<String, String> cache = new Bag<>();

    public FollowSetAggregator(Grammar grammar, Map<String, Set<String>> firstMap) {
        this.grammar = grammar;
        this.firstMap = firstMap;
    }

    public FollowSetAggregator build() {
        cache.clear();
        cache.put(grammar.getStartSymbol(), Grammar.END_SYMBOL);
        follow();
        reduce();
        return this;
    }

    public Map<String, Set<String>> getFollowMap() {
        return cache.getMap();
    }

    /**
     * replace each nonTerminal in every follow set with
     * its own follows until no set changes anymore.
     */
    private void reduce() {
        boolean changed;
        do {
            changed = false;
            for (Map.Entry<String, Set<String>> entry : cache.entrySet()) {
                final String nonTerminal = entry.getKey();
                final Set<String> follows = entry.getValue();
                changed |= cache.replaceIf(nonTerminal::equals, follows);
            }
        } while(changed);
    }

    /**
     * precalculate unreduced follow set which includes terminals, END symbol and left-hand side non-terminals.
     */
    private void follow() {
        final Set<String> nonTerminals = grammar.nonTerminals();
        grammar.rules().forEach(rule -> {
            for (String nT : nonTerminals) {
                for (int index : rule.find(nT)) {
                    followFrom(rule, nT, index);
                }
            }
        });
    }

    /**
     * follow nonTerminal - found at index - in rule and calculate and
     * add its followSet including possible left-hand side symbols.
     * @param rule the rule to follow
     * @param nonTerminal the nonTerminal for which the followSet is calculated
     * @param index the index of the nonTerminal in rule
     */
    private void followFrom(Rule rule, String nonTerminal, int index) {
        for (int i = index + 1; i < rule.size(); i++) { // iterate starting at next symbol after index
            final Set<String> first = firstMap.get(rule.get(i));
            first.stream()
                    .filter(grammar::isSymbol)
                    .forEach(symbol -> cache.put(nonTerminal, symbol));

            if (!first.contains(grammar.getEpsilonSymbol())) {
                return; // no further search needed
            }
        }
        // nonTerminal at end of rule or epsilon-derivable until end -> add left-hand side symbol
        cache.put(nonTerminal, rule.left);
    }
}
