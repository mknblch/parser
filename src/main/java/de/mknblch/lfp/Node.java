package de.mknblch.lfp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mknblch on 05.12.2015.
 */
public class Node {

    private final String identifier;
    private final List<Node> childs = new ArrayList<Node>();

    public Node(String identifier) {
        this.identifier = identifier;
    }

    Node addChild(Node node) {
        childs.add(node);
        return this;
    }

    public List<Node> getChilds() {
        return childs;
    }
}
