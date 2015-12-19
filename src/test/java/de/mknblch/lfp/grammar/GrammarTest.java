package de.mknblch.lfp.grammar;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by mknblch on 19.12.2015.
 */
public class GrammarTest {


    private static final Logger LOGGER = getLogger(GrammarTest.class);

    private static Grammar bnf4, bnf5, bnf6;

    @BeforeClass
    public static void init() throws GrammarException {
        bnf4 = GrammarReader.loadResource("bnf4.lng");
        bnf5 = GrammarReader.loadResource("bnf5.lng");
        bnf6 = GrammarReader.loadResource("bnf6.lng");
    }

    @Test
    public void testNullable() throws Exception {
        assertContains(bnf4.nullable(), "A", "B", "D", "E");
        assertContains(bnf5.nullable(), "A", "C");
        assertContains(bnf6.nullable(), "A");
    }

    @Test
    public void testIsEpsilon() throws Exception {
        assertTrue(bnf4.isEpsilon("EPS"));
        assertTrue(bnf5.isEpsilon("E"));
        assertTrue(bnf6.isEpsilon("EPSILON"));
    }

    @Test
    public void testTerminals() throws Exception {

        assertContains(bnf4.terminals(), "%a", "%b", "%c", "%d", "%e");
        assertContains(bnf5.terminals(), "%a", "%b", "%c");
        assertContains(bnf6.terminals(), "%a", "%b");
    }

    @Test
    public void testNonTerminals() throws Exception {
        assertContains(bnf4.nonTerminals(), "S", "A", "B", "C", "D", "E");
        assertContains(bnf5.nonTerminals(), "S", "A", "B", "C");
        assertContains(bnf6.nonTerminals(), "S", "A", "B");
    }


    @Test
    public void testIsSymbol() throws Exception {

    }

    @Test
    public void testRules() throws Exception {

    }

    @Test
    public void testGetStartSymbol() throws Exception {

    }

    @Test
    public void testGetEpsilonSymbol() throws Exception {

    }

    @Test
    public void testGetExclusionMap() throws Exception {

    }

    @Test
    public void testGetPatternMap() throws Exception {

    }

    @Test
    public void testGetRuleMap() throws Exception {

    }

    private void assertContains(Set<String> result, String... expected) {
        assertEquals("Length mismatch! Expected: " + String.join(",", expected) + " Got: " + result,
                expected.length, result.size());
        for (String s : expected) {
            assertTrue("Element '" + s + "' not found in " + result, result.contains(s));
        }
    }
}