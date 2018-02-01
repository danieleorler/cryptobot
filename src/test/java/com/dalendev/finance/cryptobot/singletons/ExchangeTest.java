package com.dalendev.finance.cryptobot.singletons;

import com.dalendev.finance.cryptobot.model.binance.exchange.ExchangeInfo;
import com.dalendev.finance.cryptobot.model.binance.exchange.Filters;
import com.dalendev.finance.cryptobot.test.TestUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author daniele.orler
 */
public class ExchangeTest {

    private Exchange exchange;

    @Before
    public void setUp() throws Exception {
        exchange = new Exchange();
        ExchangeInfo info = TestUtil.jsonFileToObject("e2e/exchange-info.json", ExchangeInfo.class);
        exchange.setExchangeInfo(info);
    }

    @Test
    public void getLotFilter() throws Exception {

        Filters.LotFilter result = exchange.getLotFilter("ETHBTC");
        assertEquals(Filters.FilterType.LOT_SIZE, result.getFilterType());
    }

}