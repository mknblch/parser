package de.mknblch.lfp;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.List;

import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by mknblch on 05.12.2015.
 */
public class TokenizerTest {

    private static final Logger LOGGER = getLogger(TokenizerTest.class);

    private Grammar grammar;

    @Before
    public void setUp() throws Exception {
        grammar = GrammarReader.loadResource("bnf.lng");
    }

    @Test
    public void testTokenize() throws Exception {

        final Tokenizer tokenizer = new Tokenizer(grammar);

        final List<Token> tokens = tokenizer.tokenize("hallo(25);\twelt()");
        LOGGER.debug("Tokens {}", tokens);
        assertEquals(7, tokens.size());
    }

}