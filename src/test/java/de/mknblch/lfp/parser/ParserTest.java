package de.mknblch.lfp.parser;

import de.mknblch.lfp.TestParser;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarReader;
import de.mknblch.lfp.grammar.GrammarReaderException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by mknblch on 19.12.2015.
 */
public class ParserTest {
    private static final Logger LOGGER = getLogger(GrammarAggregatorTest.class);

    private static Grammar bnf;

    @BeforeClass
    public static void init() throws GrammarReaderException {
        bnf = GrammarReader.loadResource("bnf2.lng");
    }

    @Test
    public void testParse() throws Exception {

        new TestParser(bnf).parse("aba");
        System.out.println();

    }
}