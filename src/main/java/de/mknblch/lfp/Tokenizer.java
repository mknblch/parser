package de.mknblch.lfp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mknblch on 05.12.2015.
 */
public class Tokenizer {

    private final Grammar grammar;

    public Tokenizer(Grammar grammar) {
        this.grammar = grammar;
    }

    public List<Token> tokenize(final String input) throws GrammarException {

        final ArrayList<Token> tokens = new ArrayList<>();

        mainLoop:
        for (int i = 0; i < input.length(); ) {
            final String rest = input.substring(i);
            for (Map.Entry<String, Pattern> entry : grammar.patternMap.entrySet()) {
                final Matcher matcher = entry.getValue().matcher(rest);
                if (!matcher.find() || matcher.start() != 0) {
                    continue;
                }
                i += matcher.end();
                if (!entry.getKey().startsWith(GrammarReader.EXCLUDE_PREFIX)) {
                    tokens.add(new Token(entry.getKey(), matcher.group(0)));
                }
                continue mainLoop;
            }

            throw new GrammarException("Parse Error at " +
                    i +
                    " : '" +
                    rest.substring(0, Math.min(rest.length(), 20)) +
                    "'");
        }

        return tokens;
    }

}
