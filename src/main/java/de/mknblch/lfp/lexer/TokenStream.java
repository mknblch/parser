package de.mknblch.lfp.lexer;

import de.mknblch.lfp.grammar.Grammar;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mknblch
 */
public class TokenStream {

    private final CharSequence input;
    private final Grammar grammar;


    private int offset;
    private Token current;


    TokenStream(Grammar grammar, CharSequence input) {
        this.grammar = grammar;
        this.input = input;
        offset = 0;
    }

    public Token current() {
        return current;
    }

    public boolean hasNext(){
        return offset < input.length();
    }

    public TokenStream next() throws SyntaxException {

        if (!hasNext()) {
            current = null;
            return this;
        }

        consumeExcludes(grammar.getExclusionMap());

        final CharSequence rest = input.subSequence(offset, input.length());
        for (Map.Entry<String, Pattern> entry : grammar.getPatternMap().entrySet()) {
            final Matcher matcher = match(rest, entry.getValue());
            if (null != matcher) {
                offset += matcher.end();
                current = new Token(entry.getKey(), matcher.group(0));
                return this;
            }
        }

        throw new SyntaxException("Syntax error at " +
                offset +
                " : '" +
                rest.subSequence(0, Math.min(rest.length(), 20)) +
                "'");
    }

    public int getOffset() {
        return offset;
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

    private void consumeExcludes(Map<String, Pattern> excludes) {
        doIt:
        do {
            final CharSequence rest = input.subSequence(offset, input.length());
            for (Map.Entry<String, Pattern> entry : excludes.entrySet()) {
                final Matcher matcher = match(rest, entry.getValue());
                if (null != matcher) {
                    offset += matcher.end();
                    continue doIt;
                }
            }
            return;

        } while (offset < input.length());
    }

}
