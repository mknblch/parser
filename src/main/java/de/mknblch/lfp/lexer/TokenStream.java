package de.mknblch.lfp.lexer;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mknblch
 */
public class TokenStream {

    private final CharSequence input;
    private final Map<String, Pattern> exclusionMap;
    private final Map<String, Pattern> terminalMap;

    private int offset;

    TokenStream(CharSequence input, Map<String, Pattern> terminalMap, Map<String, Pattern> exclusionMap) {
        this.input = input;
        this.exclusionMap = exclusionMap;
        this.terminalMap = terminalMap;
        offset = 0;
    }

    public boolean hasNext(){
        return offset < input.length();
    }

    public Token next() throws SyntaxException {

        consumeExcludes(exclusionMap);

        final CharSequence rest = input.subSequence(offset, input.length());
        for (Map.Entry<String, Pattern> entry : terminalMap.entrySet()) {
            final Matcher matcher = match(rest, entry.getValue());
            if (null != matcher) {
                offset += matcher.end();
                return new Token(entry.getKey(), matcher.group(0));
            }
        }

        throw new SyntaxException("Syntax error at " +
                offset +
                " : '" +
                rest.subSequence(0, Math.min(rest.length(), 20)) +
                "'");
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
