package com.dalendev.finance.cryptobot.util;

import com.dalendev.finance.cryptobot.model.Fill;
import com.dalendev.finance.cryptobot.model.Order;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author daniele.orler
 */
public class PriceUtilTest {

    @Test
    public void adjust() throws Exception {
        assertEquals(115.0, PriceUtil.adjust(114.73999786d, 1.0), 0.01);
        assertEquals(115.0f, PriceUtil.adjust(115.0d, 1.0), 0.01);
        assertEquals(2.33f, PriceUtil.adjust(2.32999992d, 0.01), 0.01);
    }

    @Test
    public void getRealPriceTwoFills() throws Exception {
        Order order = new Order("TESTBTC", Order.Side.BUY, Order.Type.MARKET, 90.0);
        order.getFills()
            .add(new Fill(4.0, 80.0));
        order.getFills()
            .add(new Fill(5.0, 10.0));

        double result = PriceUtil.getRealPrice(order);

        assertEquals(4.111, result, 0.001);

    }

    @Test
    public void getRealPriceOneFill() throws Exception {
        Order order = new Order("TESTBTC", Order.Side.BUY, Order.Type.MARKET, 90.0);
        order.getFills()
                .add(new Fill(4.0, 80.0));

        double result = PriceUtil.getRealPrice(order);

        assertEquals(4.0, result, 0.001);

    }

}