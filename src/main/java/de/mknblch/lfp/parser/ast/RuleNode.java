package de.mknblch.lfp.parser.ast;

import de.mknblch.lfp.grammar.Production;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mknblch on 19.12.2015.
 */
public class RuleNode implements Node {

    private final Production production;
    private final List<Node> childs;

    private int offset = 0;

    public RuleNode(Production production) {
        this.production = production;
        childs = new ArrayList<>(production.size());
    }

    public Production getRule() {
        return production;
    }

    public List<Node> getChilds() {
        return childs;
    }

    public void addChild(Node node) {
        if (offset >= production.size()) {
            throw new IllegalStateException("Rule " + production + " is already fulfilled");
        }
        childs.add(offset++, node);
    }

    public boolean isSatisfied() {
        return offset == production.size();
    }

    @Override
    public String toString() {
        return production.left() + ":{ "+ String.join(", ", childs.stream().map(Node::toString).collect(Collectors.toList())) + " }";
    }
}
