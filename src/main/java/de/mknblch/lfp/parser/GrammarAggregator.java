package de.mknblch.lfp.parser;

import de.mknblch.lfp.common.Bag;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.Production;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mknblch on 20.12.2015.
 */
public class GrammarAggregator {

    private final Bag<Production, String> firstRules;
    private final Bag<String, String> firstSet;
    private final Bag<String, String> followSet;
    private final Set<String> nullable;
    private final Grammar grammar;

    private boolean withEpsilon = true;
    private boolean changed;

    public GrammarAggregator(Grammar grammar) {
        this.grammar = grammar;
        firstSet = new Bag<>();
        firstRules = new Bag<>();
        followSet = new Bag<>();
        nullable = new HashSet<>();
    }

    public GrammarAggregator withEpsilon(boolean withEpsilon) {
        this.withEpsilon = withEpsilon;
        return this;
    }

    public Set<String> first(Production production) {
        return firstRules.get(production);
    }

    public Set<String> follow(String symbol) {
        return followSet.get(symbol);
    }

    public Set<String> getNullableSet() {
        return nullable;
    }

    public Bag<String, String> getFirstSet() {
        return firstSet;
    }

    public Bag<Production, String> getFirstRules() {
        return firstRules;
    }

    public Bag<String, String> getFollowSet() {
        return followSet;
    }

    public GrammarAggregator aggregate() {
        initialize(withEpsilon);
        do {
            changed = false;
            grammar.rules().forEach(this::process);
        } while (changed);
        return this;
    }

    /*
    for i = 0 to k
        if i=0 or {Y(0),...,Y(i-1)} all in nullable then
            first(X) = first(X) union first(Y(i))
        if i=k or {Y(i+1),...Y(k)} all nullable then
            follow(Y(i)) add all of follow(X)
        for j = i+1 to k
            if i+1=j or {Y(i+1),...,Y(j-1)} subset of nullable then
                follow(Y(i)) add all of first(Y(j))
     */
    private void process(Production production) {

        if (grammar.isEpsilon(production) || nullable.containsAll(production.right())) {
            addNullable(production.left());
        }
        final int k = production.size();
        for (int i = 0; i < k; i++) {
            final String symbol = production.get(i);
            if (i == 0 || nullable.containsAll(production.right().subList(0, i))) {
                addFirst(production, firstSet.get(symbol));
            }
            if (i == k || nullable.containsAll(production.right().subList(i + 1, k))) {
                addFollow(symbol, followSet.get(production.left()));
            }
            for (int j = i + 1; j < k; j++) {
                if (i + 1 == j || nullable.containsAll(production.right().subList(i + 1, j))) {
                    addFollow(symbol, firstSet.get(production.get(j)));
                }
            }
        }
    }

    private void initialize(boolean withEpsilon) {
        // reset
        nullable.clear();
        firstSet.clear();
        firstRules.clear();
        followSet.clear();
        // nullable(E) = E
        nullable.add(grammar.getEpsilonSymbol());
        // First(terminal) = terminal
        grammar.terminals().forEach(t -> firstSet.put(t, t));
        // init epsilon in nullable and first sets
        grammar.rules().stream()
                .filter(grammar::isEpsilon)
                .forEach(rule -> {
                    nullable.add(rule.left());
                    if (withEpsilon) {
                        firstSet.put(rule.left(), grammar.getEpsilonSymbol());
                        firstRules.put(rule, grammar.getEpsilonSymbol());
                    }
                });
        // Follow(S) = $
        followSet.put(grammar.getStartSymbol(), Grammar.END_SYMBOL);
    }


    private void addNullable(String symbol) {
        changed |= nullable.add(symbol);
    }

    private void addFirst(Production production, Collection<String> firsts) {
        changed |= firstSet.putAll(production.left(), firsts);
        firstRules.putAll(production, firsts);
    }

    private void addFollow(String symbol, Collection<String> firsts) {
        if (null == firsts) {
            return;
        }
        firsts.stream()
                .filter(grammar::isSymbol) // omit epsilon
                .forEach(f -> changed |= followSet.put(symbol, f));
    }

}
