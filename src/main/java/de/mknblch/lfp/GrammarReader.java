package de.mknblch.lfp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by mknblch on 04.12.2015.
 */
public class GrammarReader {

    private static final GrammarReader INSTANCE = new GrammarReader();

    public static final String DELIMITER = "::=";

    public static final String EXCLUDE_PREFIX = "#";
    public static final String TERMINAL_PREFIX = "%";
    public static final String START_PREFIX = "!";
    public static final String EPSILON = "E";

    private static final Pattern COMMENT_PATTERN = Pattern.compile("\\s*;.*");

    private static final Pattern LINE_PATTERN =
            Pattern.compile("([" + TERMINAL_PREFIX + START_PREFIX + EXCLUDE_PREFIX + "]?\\w+)\\s*"+DELIMITER+"\\s*(.+)");

    private Map<String, String> preProcess(InputStream iStream) throws GrammarException {
        final Scanner scanner = new Scanner(iStream)
                .useDelimiter(Pattern.compile("(\r?\n)+"));
        final Map<String, String> cache = new HashMap<>();
        int lineNum = 1;
        while (scanner.hasNext()) {
            final String line = scanner.next();
            if (COMMENT_PATTERN.matcher(line).matches()) {
                continue;
            }
            final Matcher matcher = LINE_PATTERN.matcher(line);
            if (!matcher.matches()) {
                throw new GrammarException("Error on Line " + lineNum + " : " + line);
            }
            cache.put(matcher.group(1), matcher.group(2));
            lineNum++;
        }
        return cache;
    }

    public Grammar readGrammar(InputStream inputStream) throws GrammarException {
        final Map<String, String> cache = preProcess(inputStream);
        final Optional<String> startSymbol = findStartSymbol(cache);
        if (!startSymbol.isPresent()) {
            throw new GrammarException("No start symbol defined");
        }
        final Map<String, Pattern> patternMap = makePatternMap(cache);
        final Map<String, List<List<String>>> ruleMap = makeRuleBag(cache);

        return new Grammar(
                startSymbol.get(),
                patternMap,
                ruleMap,
                new GrammarHelper(ruleMap)
                        .setEpsilon(EPSILON)
                        .first());
    }

    private Optional<String> findStartSymbol(Map<String, String> cache) {
        return cache.keySet().stream()
                .filter(k -> k.startsWith(START_PREFIX))
                .findFirst();
    }

    /**
     * Returns a Bag where every NonTerminal Symbol is mapped to a List of Production rules.
     * @param cache
     * @return
     */
    private Map<String, List<List<String>>> makeRuleBag(Map<String, String> cache) {
        return cache.entrySet().stream()
                    // include everything but terminal expressions
                    .filter(e -> !isTerminal(e))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            this::makeRuleList));
    }

    private boolean isTerminal(Map.Entry<String, String> e) {
        return e.getKey().startsWith(TERMINAL_PREFIX) || e.getKey().startsWith(EXCLUDE_PREFIX);
    }

    /**
     * Returns the List of production rules. One rule is represented by a String List.
     * @param entry
     * @return
     */
    private List<List<String>> makeRuleList(Map.Entry<String, String> entry) {
        final String value = entry.getValue();
        final String[] split = value.split("\\s*\\|\\s*");
        final ArrayList<List<String>> rules = new ArrayList<>(split.length);
        for (String rule : split) {
            rules.add(Arrays.asList(rule.split("\\s+")));
        }
        return rules;
    }

    private Map<String, Pattern> makePatternMap(Map<String, String> cache) {
        return cache.entrySet().stream()
                    .filter(this::isTerminal)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            (Map.Entry<String, String> e) -> Pattern.compile(e.getValue())));
    }

    public static Grammar readFromString(String input) throws GrammarException {
        return INSTANCE.readGrammar(new ByteArrayInputStream(input.getBytes(Charset.defaultCharset())));
    }

    public static Grammar readFromString(String input, Charset charset) throws GrammarException {
        return INSTANCE.readGrammar(new ByteArrayInputStream(input.getBytes(charset)));
    }

    public static Grammar load(Path path) throws IOException, GrammarException {
        final InputStream iStream = Files.newInputStream(path, StandardOpenOption.READ);
        return INSTANCE.readGrammar(iStream);
    }

    public static Grammar loadResource(String resource) throws GrammarException {
        final InputStream iStream = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resource);
        return new GrammarReader().readGrammar(iStream);
    }

}
