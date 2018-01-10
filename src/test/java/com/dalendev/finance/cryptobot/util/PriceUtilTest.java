package com.dalendev.finance.cryptobot.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author daniele.orler
 */
public class PriceUtilTest {

    @Test
    public void adjust() throws Exception {

        assertEquals(new Float(115.0f), PriceUtil.adjust(114.73999786f, 1.0f));
        assertEquals(new Float(115.0f), PriceUtil.adjust(115.0f, 1.0f));
        assertEquals(new Float(2.33f), PriceUtil.adjust(2.32999992f, 0.01f));

    }

}