package de.mknblch.lfp.parser;


import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarException;
import de.mknblch.lfp.grammar.GrammarReader;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by mknblch on 13.12.2015.
 */
public class AggregatorTest {

    @Test
    public void testFirst() throws Exception {

        final Map<String, Set<String>> first = getFirsts("bnf4.lng");
        System.out.println("FIRST: " + first);

        assertContains(first, "A", "%a", "EPS");
        assertContains(first, "B", "%b", "EPS");
        assertContains(first, "C", "%c");
        assertContains(first, "D", "%d", "EPS");
        assertContains(first, "E", "%e", "EPS");
        assertContains(first, "S", "%a", "%b", "%c", "EPS");
    }

    @Test
    public void testFollow4() throws Exception {
        final Map<String, Set<String>> follow = getFollows("bnf4.lng");
        System.out.println("FOLLOW: " + follow);
        assertContains(follow, "S", "$");
        assertContains(follow, "A", "%b", "%c");
        assertContains(follow, "B", "%c");
        assertContains(follow, "C", "%d", "%e", "$");
        assertContains(follow, "D", "%e", "$");
        assertContains(follow, "E", "$");

    }

    @Test
    public void testFollow5() throws Exception {
        final Map<String, Set<String>> follow = getFollows("bnf5.lng");
        System.out.println("FOLLOW: " + follow);
        assertContains(follow, "A", "%b");
        assertContains(follow, "B", "%c", "$");
        assertContains(follow, "C", "$");
        assertContains(follow, "S", "$");

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

    private Map<String, Set<String>> getFirsts(String file) throws GrammarException {
        return new FirstSetAggregator(GrammarReader.loadResource(file)).getFirstSets();
    }

    private Map<String, Set<String>> getFollows(String file) throws GrammarException {
        final Grammar grammar = GrammarReader.loadResource(file);
        final FirstSetAggregator firstSetAggregator = new FirstSetAggregator(grammar);
        final Map<String, Set<String>> firstSets = firstSetAggregator.getFirstSets();
        return new FollowSetAggregator(grammar, firstSets).follow().getMap();
    }
}