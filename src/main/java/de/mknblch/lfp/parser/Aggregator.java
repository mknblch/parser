package de.mknblch.lfp.parser;

import de.mknblch.lfp.common.Bag;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.Rule;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mknblch on 20.12.2015.
 */

/*
nullable = {}
foreach nonterminal X:
    first(X)={}
    follow(X)={}
for each terminal Y:
    first(Y)={Y}
    follow(Y)={}

repeat
    foreach grammar rule X ::= Y(1) ... Y(k)
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
until none of nullable,first,follow changed in last iteration
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


    }

    private void initialize() {
        //        // nullable(E) = E
        nullable.add(grammar.getEpsilonSymbol());

        // First(terminal) = terminal
        grammar.terminals().forEach(t -> firstSet.put(t, t));
        grammar.rules().stream()
                .filter(grammar::isEpsilon)
                .forEach(rule -> {
                    nullable.add(rule.left);
                    firstSet.put(rule.left, grammar.getEpsilonSymbol());
                });

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

    private void addFirst(Rule rule, Collection<String> firsts) {
        changed |= firstSet.putAll(rule.left(), firsts);
        firstRules.putAll(rule, firsts);
    }

    private void addFollow(String symbol, String first) {
        changed |= followSet.put(symbol, first);
    }

    private void addFollow(String symbol, Collection<String> firsts) {
        if (null == firsts) {
            return;
        }
        firsts.stream()
                .filter(grammar::isSymbol) // omit epsilon
                .forEach(f -> addFollow(symbol, f));
    }

    public Aggregator aggregate() {

        initialize();

        do {
            changed = false;
            grammar.rules().forEach(this::aggregate);
        } while (changed);
        return this;
    }


    /*
    for i = 0 to k
        if i=0 or {Y(0),...,Y(i-1)} subset of nullable then
            first(X) = first(X) union first(Y(i))
        if i=k or {Y(i+1),...Y(k)} subset of nullable then
            follow(Y(i)) = follow(Y(i)) union follow(X)
        for j = i+1 to k
            if i+1=j or {Y(i+1),...,Y(j-1)} subset of nullable then
                follow(Y(i)) = follow(Y(i)) union first(Y(j))
     */
    private void aggregate(Rule rule) {

        if (grammar.isEpsilon(rule) || nullable.containsAll(rule.right())) {
            addNullable(rule.left);
        }
        final int k = rule.size();
        for (int i = 0; i < k; i++) {
            final String symbol = rule.get(i);
            if (i == 0 || nullable.containsAll(rule.right().subList(0, i))) {
                addFirst(rule, firstSet.get(symbol));
            }
            if (i == k || nullable.containsAll(rule.right().subList(i + 1, k))) {
                addFollow(symbol, followSet.get(rule.left));
            }
            for (int j = i+1; j < k; j++) {
                final List<String> subl3 = rule.right().subList(i + 1, j);
                if (i + 1 == j || nullable.containsAll(subl3)) {
                    addFollow(symbol, firstSet.get(rule.get(j)));
                }
            }
        }
    }
}
