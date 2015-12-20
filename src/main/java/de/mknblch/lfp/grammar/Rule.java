package de.mknblch.lfp.grammar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author martinknobloch
 */
public class Rule implements Iterable<String> {

    public final String left;

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

    public List<Integer> find(String symbol) {

        final ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < right.size(); i++) {
            if (symbol.equals(right.get(i))) {
                list.add(i);
            }
        }
        return list;
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
