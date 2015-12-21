package de.mknblch.lfp.ast;

import de.mknblch.lfp.grammar.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mknblch on 19.12.2015.
 */
public class RuleNode implements Node {

    private final Rule rule;
    private final List<Node> childs;

    private int offset = 0;

    public RuleNode(Rule rule) {
        this.rule = rule;
        childs = new ArrayList<>(rule.size());
    }

    public Rule getRule() {
        return rule;
    }

    public List<Node> getChilds() {
        return childs;
    }

    public void addChild(Node node) {
        if (offset >= rule.size()) {
            throw new IllegalStateException("Rule " + rule + " is already fulfilled");
        }
        childs.add(offset++, node);
    }

    public boolean isSatisfied() {
        return offset == rule.size();
    }

    @Override
    public String toString() {
        return rule.left() + ":{ "+ String.join(", ", childs.stream().map(Node::toString).collect(Collectors.toList())) + " }";
    }
}
