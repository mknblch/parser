package de.mknblch.lfp.grammar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author martinknobloch
 */
public class Rule {

    private final String left;
    private final List<String> right;

    public Rule(String left, List<String> right) {
        this.left = left;
        this.right = right;
    }

    public boolean allEquals(String symbol) {
        return right.stream().allMatch(symbol::equals);
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

    public String join (String delimiter) {
        final StringBuilder builder = new StringBuilder();
        for (String symbol : right) {
            if (builder.length() != 0) {
                builder.append(delimiter);
            }
            builder.append(symbol);
        }
        return builder.toString();
    }

    public int size() {
        return right.size();
    }

    @Override
    public String toString() {
        return left + " ::= " + join(" ");
    }
}
