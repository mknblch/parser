package de.mknblch.lfp;

import org.junit.Test;

/**
 * Created by mknblch on 05.12.2015.
 */
public class GrammarReaderTest {
    
    @Test
    public void testRead() throws Exception {

        Grammar grammar = GrammarReader.loadResource("test/resources/bnf.lng");

        System.out.println(grammar);

    }
    @Test
    public void testBnf2() throws Exception {

        Grammar grammar = GrammarReader.loadResource("bnf2.lng");

        System.out.println(grammar);

    }
}