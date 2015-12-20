package de.mknblch.lfp.parser;

import de.mknblch.lfp.grammar.Rule;
import de.mknblch.lfp.lexer.Token;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by mknblch on 19.12.2015.
 */
public class NodeBuilder {

    private final String startSymbol;


    public NodeBuilder(String startSymbol) {

        this.startSymbol = startSymbol;
    }

    public void reduce(String terminal) {

    }

    public void reduce(Rule rule) {

    }
}
