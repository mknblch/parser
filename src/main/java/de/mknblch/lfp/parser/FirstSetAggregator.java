package de.mknblch.lfp.parser;

import de.mknblch.lfp.common.Bag;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarException;
import de.mknblch.lfp.grammar.Rule;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mknblch on 13.12.2015.
 */
public class FirstSetAggregator {

    private final Grammar grammar;

    private final Bag<String, String> cache = new Bag<>();

    public FirstSetAggregator(Grammar grammar) {
        this.grammar = grammar;
    }

    public Map<String, Set<String>> getFirstSet() {
        return cache.getMap();
    }

    public FirstSetAggregator build() throws GrammarException {

        grammar.terminals()
                .forEach(s -> cache.put(s, s));

        grammar.getRuleMap()
                .values()
                .stream()
                .flatMap(List::stream)
                .forEach(rule -> cache.putAll(rule.left, first(rule)));

        return this;
    }

    private Set<String> first(String symbol) {
        final Set<String> cached = cache.get(symbol);
        if (null != cached) {
            return cached;
        }

        if (grammar.isNonTerminal(symbol)) {
            return grammar.getRuleMap()
                    .get(symbol)
                    .stream()
                    .map(this::first)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }

        return new HashSet<String>() {{add(symbol);}};
    }

    private Set<String> first(Rule rule) {
        final Set<String> ret = new HashSet<>();
        for (int index = 0; index < rule.size(); index++) {
            final Set<String> first = first(rule.get(index));
            ret.addAll(first);
            if (!first.contains(grammar.getEpsilonSymbol())) {
                return ret;
            }
        }
        return ret;
    }
}
