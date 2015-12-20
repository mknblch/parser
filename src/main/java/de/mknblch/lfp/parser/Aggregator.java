package de.mknblch.lfp.parser;

import de.mknblch.lfp.common.Bag;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.Rule;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mknblch on 20.12.2015.
 */
public class Aggregator {

    private final Bag<Rule, String> firstRules;
    private final Bag<String, String> firstSet;
    private final Bag<String, String> followSet;
    private final Set<String> nullable;
    private final Grammar grammar;

    private boolean changed;


    public Aggregator(Grammar grammar) {
        this.grammar = grammar;
        firstSet = new Bag<>();
        firstRules = new Bag<>();
        followSet = new Bag<>();
        nullable = new HashSet<>();

        // First(terminal) = terminal
        grammar.terminals().forEach(t -> firstSet.put(t, t));

        // Follow(S) = $
        followSet.put(grammar.getStartSymbol(), Grammar.END_SYMBOL);
    }

    public Set<String> getNullable() {
        return nullable;
    }

    public Bag<String, String> getFirstSet() {
        return firstSet;
    }

    public Bag<Rule, String> getFirstRules() {
        return firstRules;
    }

    public Bag<String, String> getFollowSet() {
        return followSet;
    }

    private void addNullable(String symbol) {
        changed |= nullable.add(symbol);
    }

    private void addFirst(Rule rule, String first) {
        changed |= firstSet.put(rule.left, first);
        firstRules.put(rule, first);
    }

    private void addAllFirst(Rule rule, Collection<String> firsts) {
        changed |= firstSet.putAll(rule.left, firsts);
        firstRules.putAll(rule, firsts);
    }

    private void addFollow(String symbol, String first) {
        changed |= followSet.put(symbol, first);
    }

    private void addFollow(String symbol, Collection<String> firsts) {
        changed |= followSet.putAll(symbol, firsts);
    }

    /*

repeat
  foreach grammar rule X ::= Y(1) ... Y(k)

    ...
until none of nullable,first,follow changed in last iteration
     */
    public void aggregate() {
        do {
            changed = false;
            grammar.rules().forEach(this::aggregate);
        } while (changed);

        enhance();
    }

    private void enhance() {

        nullable.forEach(s -> firstSet.put(s, grammar.getEpsilonSymbol()));
    }

    /*
    if k=0 or {Y(1),...,Y(k)} subset of nullable then
        nullable = nullable union {X}
    for i = 1 to k
        if i=1 or {Y(1),...,Y(i-1)} subset of nullable then
            first(X) = first(X) union first(Y(i))
        for j = i+1 to k
            if i=k or {Y(i+1),...Y(k)} subset of nullable then
                follow(Y(i)) = follow(Y(i)) union follow(X)
            if i+1=j or {Y(i+1),...,Y(j-1)} subset of nullable then
                follow(Y(i)) = follow(Y(i)) union first(Y(j))
     */
    private void aggregate(Rule rule) {

        aggregateNullable(rule);
        aggregateFirst(rule);
        aggregateFollow(rule);
    }

    private void aggregateFollow(Rule rule) {

        final int k = rule.size();

        for (int i = 0; i < k; i++) {
            final String symbol = rule.get(i);
            for (int j = i+1; j < k; j++) {
                if (i == k-1 || nullable.containsAll(rule.right().subList(i+1, k))) {
                    addFollow(symbol, followSet.get(rule.left));
                }

                if (i + 1 == j || nullable.containsAll(rule.right().subList(i+1, j-1))) {
                    addFollow(symbol, firstSet.get(rule.get(j)));
                }
            }
        }
    }

    private void aggregateFirst(Rule rule) {
        for (int i = 0; i < rule.size(); i++) {
            final String symbol = rule.get(i);
            if (grammar.isTerminal(symbol)) {
                addFirst(rule, symbol);
                return;
            }
            if (grammar.isNonTerminal(symbol)) {
                addAllFirst(rule, firstSet.get(symbol));
                if (!nullable.contains(symbol)) {
                    return;
                }
            }
        }
    }

    private void aggregateNullable(Rule rule) {
        if (grammar.isEpsilon(rule) || nullable.containsAll(rule.right())) {
            addNullable(rule.left);
        }
    }


}
