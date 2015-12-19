package de.mknblch.lfp.parser;

import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarException;
import de.mknblch.lfp.grammar.GrammarReader;
import de.mknblch.lfp.lexer.Lexer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by mknblch on 19.12.2015.
 */
public class ParserTest {
    private static final Logger LOGGER = getLogger(AggregatorTest.class);

    private static Grammar bnf1, bnf2, bnf3, bnf4, bnf5, bnf6;

    @BeforeClass
    public static void init() throws GrammarException {
        bnf6 = GrammarReader.loadResource("bnf6.lng");
    }

    @Test
    public void testParse() throws Exception {

        final Parser parser = new Parser(bnf6);

        parser.parse("aba");
    }
}