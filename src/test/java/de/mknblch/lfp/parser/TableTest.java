package de.mknblch.lfp.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mknblch on 11.12.2015.
 */
public class TableTest {

    private final Table<Integer, Integer, String> table = new Table<>();

    @Before
    public void setup() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                table.put(i, j, i + ":" + j);
            }
        }
    }

    @Test
    public void testTable() throws Exception {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(i + ":" + j, table.get(i, j));
            }
        }
    }

    @Test
    public void testOutOfBounds() throws Exception {
        assertNull(table.get(11, 11));
    }
}