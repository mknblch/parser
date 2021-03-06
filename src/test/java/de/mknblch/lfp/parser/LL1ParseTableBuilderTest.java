package de.mknblch.lfp.parser;

import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarLoader;
import de.mknblch.lfp.grammar.GrammarLoaderException;
import de.mknblch.lfp.parser.ll1.LL1ParseTableBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by mknblch on 19.12.2015.
 */
public class LL1ParseTableBuilderTest {

    private static final Logger LOGGER = getLogger(GrammarAggregatorTest.class);

    private static Grammar bnf1, bnf2, bnf3, bnf4, bnf5, bnf6;

    @BeforeClass
    public static void init() throws GrammarLoaderException {
        bnf1 = GrammarLoader.loadResource("bnf.lng");
        bnf2 = GrammarLoader.loadResource("bnf2.lng");
        bnf3 = GrammarLoader.loadResource("bnf3.lng");
        bnf4 = GrammarLoader.loadResource("bnf4.lng");
        bnf5 = GrammarLoader.loadResource("bnf5.lng");
        bnf6 = GrammarLoader.loadResource("bnf6.lng");
    }

    @Test
    public void testBnf4() throws Exception {
        testGrammar(bnf4);
    }

    @Test
    public void testBnf5() throws Exception {
        testGrammar(bnf5);
    }

    @Test
    public void testBnf3() throws Exception {
        testGrammar(bnf3);
    }

    @Test
    public void testBnf2() throws Exception {
        testGrammar(bnf2);
    }

    private void testGrammar(Grammar grammar) throws GrammarException {
        System.out.println("------------------------------------");
        System.out.println(grammar);
        System.out.println(new LL1ParseTableBuilder(grammar).build());
    }
}