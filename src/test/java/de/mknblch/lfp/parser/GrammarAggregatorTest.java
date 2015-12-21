package de.mknblch.lfp.parser;


import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarReader;
import de.mknblch.lfp.grammar.GrammarReaderException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by mknblch on 13.12.2015.
 */
public class GrammarAggregatorTest {

    private static final Logger LOGGER = getLogger(GrammarAggregatorTest.class);

    private static Grammar bnf4, bnf5, err;

    @BeforeClass
    public static void init() throws  GrammarReaderException {
        bnf4 = GrammarReader.loadResource("bnf4.lng");
        bnf5 = GrammarReader.loadResource("bnf5.lng");
        err = GrammarReader.loadResource("err.lng");
    }

    @Test
    public void testFirstErr() throws Exception {

        final Map<String, Set<String>> first = getFirsts(err);
        LOGGER.info("FIRST: {}", first);
    }

    @Test
    public void testFirst4() throws Exception {

        final Map<String, Set<String>> first = getFirsts(bnf4);
        LOGGER.info("FIRST: {}", first);
        assertContains(first, "A", "%a", "EPS");
        assertContains(first, "B", "%b", "EPS");
        assertContains(first, "C", "%c");
        assertContains(first, "D", "%d", "EPS");
        assertContains(first, "E", "%e", "EPS");
        assertContains(first, "S", "%a", "%b", "%c", "EPS");
    }

    @Test
    public void testFirst5() throws Exception {

        final Map<String, Set<String>> first = getFirsts(bnf5);
        LOGGER.info("FIRST: {}", first);
        assertContains(first, "A", "%a", "E");
        assertContains(first, "B", "%b");
        assertContains(first, "C", "%c", "E");
        assertContains(first, "S", "%a", "%b", "E");
    }

    @Test
    public void testFollow4() throws Exception {
        final Map<String, Set<String>> follow = getFollows(bnf4);
        LOGGER.info("FOLLOW: {}", follow);
        assertContains(follow, "S", "$");
        assertContains(follow, "A", "%b", "%c");
        assertContains(follow, "B", "%c");
        assertContains(follow, "C", "%d", "%e", "$");
        assertContains(follow, "D", "%e", "$");
        assertContains(follow, "E", "$");

    }

    @Test
    public void testFollow5() throws Exception {
        final Map<String, Set<String>> follow = getFollows(bnf5);
        LOGGER.info("FOLLOW: {}", follow);
        assertContains(follow, "A", "%b");
        assertContains(follow, "B", "%c", "$");
        assertContains(follow, "S", "$");
        assertContains(follow, "C", "$");
    }

    private void assertContains(Map<String, Set<String>> result, String symbol, String... expected) {
        final Set<String> set = result.get(symbol);
        assertNotNull(symbol + " not found", set);
        assertEquals("Length mismatch in " + symbol + "! Expected: " + String.join(",", expected) + " Got: " + set,
                expected.length, set.size());
        for (String s : expected) {
            assertTrue("Element '" + s + "' not found in Rule " + symbol + " -> " + set, set.contains(s));
        }
    }

    private Map<String, Set<String>> getFirsts(Grammar grammar) throws GrammarException {

        return new GrammarAggregator(grammar).aggregate().getFirstSet().getMap();


//        return new FirstSetAggregator(grammar).aggregate().getFirstSet();
    }

    private Map<String, Set<String>> getFollows(Grammar grammar) throws GrammarException {

        return new GrammarAggregator(grammar).aggregate().getFollowSet().getMap();

//        return new FollowSetAggregator(grammar, new FirstSetAggregator(grammar).aggregate())
//                .aggregate()
//                .getFollowMap();
    }
}