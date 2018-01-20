package com.dalendev.finance.cryptobot.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author daniele.orler
 */
public class PriceUtilTest {

    @Test
    public void adjust() throws Exception {
        assertEquals(new Double(115.0f), PriceUtil.adjust(114.73999786d, 1.0d));
        assertEquals(new Double(115.0f), PriceUtil.adjust(115.0d, 1.0d));
        assertEquals(new Double(2.33f), PriceUtil.adjust(2.32999992d, 0.01d));
    }

}