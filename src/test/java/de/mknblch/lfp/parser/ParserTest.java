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
        bnf2 = GrammarReader.loadResource("bnf2.lng");
        bnf3 = GrammarReader.loadResource("bnf3.lng");
        bnf4 = GrammarReader.loadResource("bnf4.lng");
        bnf5 = GrammarReader.loadResource("bnf5.lng");
        bnf6 = GrammarReader.loadResource("bnf6.lng");
    }

    @Test
    public void testParse() throws Exception {

        System.out.println(bnf3);
        final Parser parser = new Parser(bnf3);

        parser.parse("(3*(3+1) + (1+1))");
    }
}