package com.dalendev.finance.cryptobot.model;

import com.dalendev.finance.cryptobot.test.TestUtil;
import com.dalendev.finance.cryptobot.util.PriceUtil;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author daniele.orler
 */
public class OrderTest {

    @Test
    public void deserializeOrderTest() throws Exception {
        Order order = TestUtil.jsonFileToObject("full-order-result.json", Order.class);
        assertEquals(5, order.getFills().size());

        double price = PriceUtil.getRealPrice(order);
        assertEquals(3998.3, price, 0.1);
    }

    @Test
    public void deserializeOrderOneFillTest() throws Exception {
        Order order = TestUtil.jsonFileToObject("full-order-result-one-fill.json", Order.class);
        assertEquals(1, order.getFills().size());

        double price = PriceUtil.getRealPrice(order);
        assertEquals(0.017682, price, 0.0000001);
        assertEquals(0.057, order.getFills().get(0).getQuantity(), 0.001);
    }

    @Test
    public void deserializeEmptyOrder() throws Exception {
        Order order = TestUtil.jsonStringToObject("{}", Order.class);
        assertNull(order.getSymbol());
    }

}