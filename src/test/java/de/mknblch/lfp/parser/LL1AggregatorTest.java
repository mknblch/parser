package de.mknblch.lfp.parser;

import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarException;
import de.mknblch.lfp.grammar.GrammarReader;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by mknblch on 19.12.2015.
 */
public class LL1AggregatorTest {

    private static final Logger LOGGER = getLogger(AggregatorTest.class);

    private static Grammar bnf1, bnf2, bnf3, bnf4, bnf5, bnf6;

    @BeforeClass
    public static void init() throws GrammarException {
        bnf1 = GrammarReader.loadResource("bnf.lng");
        bnf2 = GrammarReader.loadResource("bnf2.lng");
        bnf3 = GrammarReader.loadResource("bnf3.lng");
        bnf4 = GrammarReader.loadResource("bnf4.lng");
        bnf5 = GrammarReader.loadResource("bnf5.lng");
        bnf6 = GrammarReader.loadResource("bnf6.lng");
    }

    @Test
    public void testBnf4() throws Exception {
        LOGGER.info("BNF4\n{}", new LL1Aggregator(bnf4).build()
                .getParseTable());
    }

    @Test
    public void testBnf5() throws Exception {
        LOGGER.info("BNF5\n{}", new LL1Aggregator(bnf5).build()
                .getParseTable());
    }

    @Test
    public void testBnf6() throws Exception {
        LOGGER.info("BNF6\n{}", new LL1Aggregator(bnf6).build()
                .getParseTable());
    }
}