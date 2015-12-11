package de.mknblch.lfp.grammar;

import java.util.Arrays;
import java.util.List;

/**
 * @author martinknobloch
 */
public class Rule {

    private final List<String> symbols;

    public Rule(List<String> symbols) {
        this.symbols = symbols;
    }

    public Rule(String[] symbols) {
        this.symbols = Arrays.asList(symbols);
    }

    public boolean allEquals(String symbol) {
        return symbols.stream().allMatch(symbol::equals);
    }

    public int indexOf(String symbol) {
        return symbols.indexOf(symbol);
    }

    public String get(int index) {
        return symbols.get(index);
    }

    public String join (String delimiter) {
        final StringBuilder builder = new StringBuilder();
        for (String symbol : symbols) {
            if (builder.length() != 0) {
                builder.append(delimiter);
            }
            builder.append(symbol);
        }
        return builder.toString();
    }

    public int size() {
        return symbols.size();
    }

    @Override
    public String toString() {
        return join(" ");
    }
}
