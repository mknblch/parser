package de.mknblch.lfp;

import de.mknblch.lfp.parser.FirstFollowCalc;
import de.mknblch.lfp.grammar.GrammarReader;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by mknblch on 07.12.2015.
 */
public class FirstFollowCalcTest {
    
    private FirstFollowCalc firstFollowCalc;

    @Before
    public void setUp() throws Exception {
        firstFollowCalc = new FirstFollowCalc(GrammarReader.loadResource("bnf3.lng"));
    }

    @Test
    public void testFirst() throws Exception {

        System.out.println(firstFollowCalc.first());

    }

    @Test
    public void testFollow() throws Exception {
        System.out.println(firstFollowCalc.follow());

    }
}