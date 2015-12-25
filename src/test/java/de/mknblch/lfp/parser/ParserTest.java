package de.mknblch.lfp.parser;

import de.mknblch.lfp.grammar.GrammarLoaderException;
import de.mknblch.lfp.parser.ast.Node;
import de.mknblch.lfp.parser.ll1.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by mknblch on 19.12.2015.
 */
public class ParserTest {

    private static final Logger LOGGER = getLogger(GrammarAggregatorTest.class);

    private static Parser parser;

    @BeforeClass
    public static void setup() throws GrammarLoaderException, GrammarException {
        parser = new ParserBuilder()
                .withResource("err.lng")
                .build();
    }

    @Test
    public void testParse() throws Exception {



        final Node node = parser
                .parse("aba");

        LOGGER.info(node.toString());


    }
}