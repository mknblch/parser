package de.mknblch.lfp;

import de.mknblch.lfp.parser.FirstFollowCalc;
import de.mknblch.lfp.grammar.GrammarReader;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by mknblch on 07.12.2015.
 */
public class FirstFollowCalcTest {
    
    private FirstFollowCalc firstFollowCalc;

    @Before
    public void setUp() throws Exception {
        firstFollowCalc = new FirstFollowCalc(GrammarReader.loadResource("bnf4.lng"));
    }

    @Test
    public void testFirst() throws Exception {

        final Map<String, Set<String>> first = firstFollowCalc.first();
        System.out.println("FIRST: " + first);

        assertContains(first.get("A"), "%a", "EPS");
        assertContains(first.get("B"), "%b", "EPS");
        assertContains(first.get("C"), "%c");
        assertContains(first.get("D"), "%d", "EPS");
        assertContains(first.get("E"), "%e", "EPS");
        assertContains(first.get("S"), "%a", "%b", "%c", "EPS");
    }

    @Test
    public void testFollow() throws Exception {
        final Map<String, Set<String>> follow = firstFollowCalc.follow();
        System.out.println("FOLLOW: " + follow);
        assertContains(follow.get("A"), "%b", "%c");
        assertContains(follow.get("B"), "%c");
        assertContains(follow.get("C"), "%d", "%e", "$");
        assertContains(follow.get("D"), "%e", "$");
        assertContains(follow.get("S"), "$");
        assertContains(follow.get("E"), "$");

    }

    private void assertContains(Set<String> result, String... expected) {
        assertEquals("Length mismatch! Expected: " + String.join(",", expected) + " Got: " + result,
                expected.length, result.size());
        for (String s : expected) {
            assertTrue("Element not found: " + s, result.contains(s));
        }
    }
}