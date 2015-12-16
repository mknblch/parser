package de.mknblch.lfp.lexer;

import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mknblch on 05.12.2015.
 */
public class Lexer {

    private final Grammar grammar;

    public Lexer(Grammar grammar) {
        this.grammar = grammar;
    }

    public List<Token> tokenize(final String input) throws GrammarException {

        final ArrayList<Token> tokens = new ArrayList<>();

        outer:
        for (int i = 0; i < input.length(); ) {
            final String rest = input.substring(i);

            // cut out expressions to exclude
            for (Map.Entry<String, Pattern> entry : grammar.getExclusionMap().entrySet()) {
                final Matcher matcher = match(rest, entry.getValue());
                if (null == matcher) {
                    continue;
                }
                i += matcher.end();
                continue outer;
            }
            // match tokens
            for (Map.Entry<String, Pattern> entry : grammar.getPatternMap().entrySet()) {
                final Matcher matcher = match(rest, entry.getValue());
                if (null == matcher) {
                    continue;
                }
                i += matcher.end();
                tokens.add(new Token(entry.getKey(), matcher.group(0)));
                continue outer;
            }
            // no possible rule found, throw exception
            throw new GrammarException("Parse Error at " +
                    i +
                    " : '" +
                    rest.substring(0, Math.min(rest.length(), 20)) +
                    "'");
        }

        return tokens;
    }

    private Matcher match(CharSequence input, Pattern pattern) {
        final Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            return null;
        }
        if (matcher.start() != 0) {
            return null;
        }
        // matched at first char of input
        return matcher;
    }

}
