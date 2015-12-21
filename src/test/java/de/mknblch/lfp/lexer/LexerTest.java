package de.mknblch.lfp.lexer;

import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarReader;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.List;

import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by mknblch on 05.12.2015.
 */
public class LexerTest {

    private static final Logger LOGGER = getLogger(LexerTest.class);

    private Grammar grammar;

    @Before
    public void setUp() throws Exception {
        grammar = GrammarReader.loadResource("bnf.lng");
    }

    @Test
    public void testTokenize() throws Exception {

        final Lexer lexer = new Lexer(grammar);

        final List<Token> tokens = lexer.tokenize("hallo(25);\twelt()");
        LOGGER.debug("Tokens {}", tokens);
        assertEquals(8, tokens.size());
    }

}