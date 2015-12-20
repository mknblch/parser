package de.mknblch.lfp.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mknblch on 19.12.2015.
 */
public class RuleNode implements Node {

    private final String symbol;
    private List<Node> childs = new ArrayList<>();

    public RuleNode(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void addChild(Node child) {
        childs.add(child);
    }

    public List<Node> getChilds() {
        return childs;
    }
}
