package de.mknblch.lfp.grammar;

import java.util.Iterator;
import java.util.List;

/**
 * @author martinknobloch
 */
public class Rule implements Iterable<String> {

    private final String left;
    private final List<String> right;

    public Rule(String left, List<String> right) {
        this.left = left;
        this.right = right;
    }

    public String left() {
        return left;
    }

    public List<String> right() {
        return right;
    }

    public String get(int index) {
        return right.get(index);
    }

    public int size() {
        return right.size();
    }

    @Override
    public String toString() {
        return left + " -> " + String.join(" ", right);
    }

    @Override
    public Iterator<String> iterator() {
        return right.iterator();
    }

}
