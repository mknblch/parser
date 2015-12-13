package de.mknblch.lfp.parser;

import de.mknblch.lfp.grammar.Grammar;

import java.util.*;

/**
 * Created by mknblch on 13.12.2015.
 */
public class FollowHelper {

    private final Grammar grammar;

    private final Map<String, List<String>> followSet = new HashMap<>();

    public FollowHelper(Grammar grammar) {
        this.grammar = grammar;
    }

    private void addFollow(String nonTerminal, String follow) {
        List<String> follows = followSet.get(nonTerminal);
        if (null == follows) {
            follows = new ArrayList<>();
        }
        follows.add(follow);
    }



    public Map<String, List<String>> build() {

        final Set<String> nonTerminals = grammar.nonTerminals();




        return followSet;
    }
}
