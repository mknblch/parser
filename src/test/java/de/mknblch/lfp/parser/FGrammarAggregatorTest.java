package de.mknblch.lfp.parser;

import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarReader;
import de.mknblch.lfp.grammar.GrammarReaderException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by mknblch on 20.12.2015.
 */
public class FGrammarAggregatorTest {

    private static final Logger LOGGER = getLogger(GrammarAggregatorTest.class);

    private static Grammar bnf;

    @BeforeClass
    public static void init() throws GrammarReaderException {
        bnf = GrammarReader.loadResource("bnf5.lng");
    }

    @Test
    public void test() throws Exception {

        System.out.println(bnf);

        final GrammarAggregator aggregator = new GrammarAggregator(bnf);

        aggregator.aggregate();

        System.out.println("NULLABLE: " + aggregator.getNullableSet());
        System.out.println("FIRST: " + aggregator.getFirstSet());
        System.out.println("FIRSTRULES: " + aggregator.getFirstRules());
        System.out.println("FOLLOWS: " + aggregator.getFollowSet());
    }
    
}