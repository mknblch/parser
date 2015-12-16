package de.mknblch.lfp.parser;

import de.mknblch.lfp.common.Bag;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by mknblch on 16.12.2015.
 */
public class BagTest {
    
    @Test
    public void testForEachValue() throws Exception {

        final Bag<Integer, String> bag = new Bag<>();

        bag.putAll(0, Arrays.asList("a", "b", "c"))
                .forEachValue(System.out::println);

    }
}