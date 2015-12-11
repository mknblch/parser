package de.mknblch.lfp;

import de.mknblch.lfp.parser.GrammarHelper;
import de.mknblch.lfp.grammar.GrammarReader;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by mknblch on 07.12.2015.
 */
public class GrammarHelperTest {
    
    private GrammarHelper grammarHelper;

    @Before
    public void setUp() throws Exception {
        grammarHelper = new GrammarHelper(GrammarReader.loadResource("bnf2.lng"));
    }

    @Test
    public void testFirst() throws Exception {

        System.out.println(grammarHelper.first());

    }

    @Test
    public void testFollow() throws Exception {
        System.out.println(grammarHelper.follow());

    }
}