package de.mknblch.lfp.grammar;

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
import java.util.stream.Stream;

/**
 * Created by mknblch on 04.12.2015.
 */
public class GrammarReader {

    private static final GrammarReader INSTANCE = new GrammarReader();

    public static final String DELIMITER = "::=";

    public static final String EXCLUDE_PREFIX = "~";
    public static final String OPTION_PREFIX = "#";
    public static final String TERMINAL_PREFIX = "%";

    public static final String START_OPTION = "#START";
    public static final String EPSILON_OPTION = "#EPSILON";

    private static final Pattern COMMENT_PATTERN = Pattern.compile("\\s*;.*");
    private static final Pattern LINE_PATTERN =
            Pattern.compile("([" + TERMINAL_PREFIX + OPTION_PREFIX + EXCLUDE_PREFIX + "]?\\w+)\\s*"+DELIMITER+"\\s*(.+)");

    private Map<String, String> preProcess(InputStream iStream) throws GrammarException {
        final Scanner scanner = new Scanner(iStream)
                .useDelimiter(Pattern.compile("(\r?\n)+"));
        final Map<String, String> cache = new HashMap<>();
        while (scanner.hasNext()) {
            final String line = scanner.next();
            if (COMMENT_PATTERN.matcher(line).matches()) {
                continue;
            }
            final Matcher matcher = LINE_PATTERN.matcher(line);
            if (!matcher.matches()) {
                throw new GrammarException("Error at : " + line);
            }
            cache.put(matcher.group(1), matcher.group(2));
        }
        return cache;
    }

    public Grammar readGrammar(InputStream inputStream) throws GrammarException {

        final Map<String, String> cache = preProcess(inputStream);

        final Map<String, String> properties = makeProperties(cache);
        final Map<String, Pattern> patternMap = makePatternMap(cache);
        final Map<String, Pattern> exclusionMap = makeExclusionMap(cache);
        final Map<String, List<Rule>> ruleMap = makeRuleBag(cache);

        return new Grammar(
                properties.get(START_OPTION),
                properties.get(EPSILON_OPTION),
                exclusionMap,
                patternMap,
                ruleMap);
    }

    private Map<String, String> makeProperties(Map<String, String> cache) {
        final Map<String, String> properties = new HashMap<>();
        cache.entrySet().stream()
                .filter(this::isProperty)
                .forEach(e -> properties.put(e.getKey(), e.getValue()));
        return properties;
    }

    private boolean isProperty(Map.Entry<String, String> e) {
        return e.getKey().startsWith(OPTION_PREFIX);
    }

    private boolean isTerminal(Map.Entry<String, String> e) {
        return e.getKey().startsWith(TERMINAL_PREFIX);
    }

    private boolean isExcludeToken(Map.Entry<String, String> e) {
        return e.getKey().startsWith(EXCLUDE_PREFIX);
    }

    private boolean isNonTerminal(Map.Entry<String, String> e) {
        return !isProperty(e) && !isExcludeToken(e) && !isTerminal(e);
    }

    /**
     * Returns a Bag where every NonTerminal Symbol is mapped to a List of Production rules.
     * @param cache
     * @return
     */
    private Map<String, List<Rule>> makeRuleBag(Map<String, String> cache) {
        return cache.entrySet().stream()
                // filter non terminals
                .filter(this::isNonTerminal)
                .collect(Collectors.toMap(Map.Entry::getKey, this::makeRuleList));
    }

    /**
     * Returns the List of production rules.
     * @param entry
     * @return
     */
    private List<Rule> makeRuleList(Map.Entry<String, String> entry) {

        final Pattern symbolSplitter = Pattern.compile("\\s+");
        final Pattern ruleSplitter = Pattern.compile("\\s*\\|\\s*");

        return Stream.of(ruleSplitter.split(entry.getValue()))
                .map(symbolSplitter::split)
                .map(Arrays::asList)
                .map(l -> new Rule(entry.getKey(), l))
                .collect(Collectors.toList());
    }

    private Map<String, Pattern> makePatternMap(Map<String, String> cache) {
        return cache.entrySet().stream()
                    .filter(this::isTerminal)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            (Map.Entry<String, String> e) -> Pattern.compile(e.getValue())));
    }

    private Map<String, Pattern> makeExclusionMap(Map<String, String> cache) {
        return cache.entrySet().stream()
                    .filter(this::isExcludeToken)
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
