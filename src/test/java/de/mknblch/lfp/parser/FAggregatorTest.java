package de.mknblch.lfp.parser;

import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarException;
import de.mknblch.lfp.grammar.GrammarReader;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by mknblch on 20.12.2015.
 */
public class FAggregatorTest {

    private static final Logger LOGGER = getLogger(AggregatorTest.class);

    private static Grammar bnf;

    @BeforeClass
    public static void init() throws GrammarException {
        bnf = GrammarReader.loadResource("bnf5.lng");
    }

    @Test
    public void test() throws Exception {

        System.out.println(bnf);

        final Aggregator aggregator = new Aggregator(bnf);

        aggregator.aggregate();

        System.out.println("NULLABLE: " + aggregator.getNullable());
        System.out.println("FIRST: " + aggregator.getFirstSet());
        System.out.println("FIRSTRULES: " + aggregator.getFirstRules());
        System.out.println("FOLLOWS: " + aggregator.getFollowSet());
    }
    
}