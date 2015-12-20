package de.mknblch.lfp.parser;

import de.mknblch.lfp.common.Bag;
import de.mknblch.lfp.common.Table;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarException;
import de.mknblch.lfp.grammar.Rule;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mknblch on 20.12.2015.
 */

/*
http://lara.epfl.ch/w/cc09:algorithm_for_first_and_follow_sets

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

    private boolean withEpsilon = true;
    private boolean changed;

    public Aggregator(Grammar grammar) {
        this.grammar = grammar;
        firstSet = new Bag<>();
        firstRules = new Bag<>();
        followSet = new Bag<>();
        nullable = new HashSet<>();
    }

    public Aggregator withEpsilon(boolean withEpsilon) {
        this.withEpsilon = withEpsilon;
        return this;
    }

    public Set<String> first(String symbol) {
        return firstSet.get(symbol);
    }

    public Set<String> first(Rule rule) {
        return firstRules.get(rule);
    }

    public Set<String> follow(String symbol) {
        return followSet.get(symbol);
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

    public Aggregator aggregate() {

        initialize(withEpsilon);

        do {
            changed = false;
            grammar.rules().forEach(this::process);
        } while (changed);
        return this;
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

        //
        grammar.rules().stream()
                .filter(grammar::isEpsilon)
                .forEach(rule -> {
                    nullable.add(rule.left);
                    if (withEpsilon) {
                        firstSet.put(rule.left, grammar.getEpsilonSymbol());
                        firstRules.put(rule, grammar.getEpsilonSymbol());
                    }
                });

        // Follow(S) = $
        followSet.put(grammar.getStartSymbol(), Grammar.END_SYMBOL);
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
    private void process(Rule rule) {

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
            for (int j = i + 1; j < k; j++) {
                final List<String> subl3 = rule.right().subList(i + 1, j);
                if (i + 1 == j || nullable.containsAll(subl3)) {
                    addFollow(symbol, firstSet.get(rule.get(j)));
                }
            }
        }
    }


    private void addNullable(String symbol) {
        changed |= nullable.add(symbol);
    }

    private void addFirst(Rule rule, Collection<String> firsts) {
        changed |= firstSet.putAll(rule.left(), firsts);
        firstRules.putAll(rule, firsts);
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
