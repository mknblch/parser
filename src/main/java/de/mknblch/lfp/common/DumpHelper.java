package de.mknblch.lfp.common;

import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.Rule;

import java.util.List;

/**
 * Created by mknblch on 19.12.2015.
 */
public class DumpHelper {

    public static String getGrammarDump(Grammar grammar) {
        final StringBuilder buffer = new StringBuilder();

        buffer.append("OPTIONS:").append("\n");

        buffer.append("\tSTART -> ").append(grammar.getStartSymbol()).append("\n");
        buffer.append("\tEPSILON -> ").append(grammar.getEpsilonSymbol()).append("\n");

        buffer.append("EXCLUDES:\n");
        for (String symbol : grammar.getExclusionMap().keySet()) {
            buffer.append("\t")
                    .append(symbol)
                    .append(" -> ")
                    .append(grammar.getExclusionMap().get(symbol))
                    .append("\n");
        }
        buffer.append("TERMINALS:\n");
        for (String terminal : grammar.getPatternMap().keySet()) {
            buffer.append("\t")
                    .append(terminal)
                    .append(" -> ")
                    .append(grammar.getPatternMap().get(terminal))
                    .append("\n");
        }
        buffer.append("RULES:\n");
        for (String nonTerminal : grammar.getRuleMap().keySet()) {
            buffer.append("\t")
                    .append(nonTerminal)
                    .append(" -> ")
                    .append(getRuleDump(grammar, nonTerminal))
                    .append("\n");
        }

        return buffer.toString();
    }

    private static String getRuleDump(Grammar grammar, String nonTerminal) {
        final List<Rule> rules = grammar.getRuleMap().get(nonTerminal);
        final StringBuilder buffer = new StringBuilder();

        for (Rule rule : rules) {
            if (buffer.length() != 0) {
                buffer.append(" | ");
            }
            buffer.append(String.join(" ", rule.right()));
        }
        return buffer.toString();
    }

}

