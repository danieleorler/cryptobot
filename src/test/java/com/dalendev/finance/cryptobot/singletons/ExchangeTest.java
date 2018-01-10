package com.dalendev.finance.cryptobot.singletons;

import com.dalendev.finance.cryptobot.model.binance.exchange.ExchangeInfo;
import com.dalendev.finance.cryptobot.model.binance.exchange.Filters;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * @author daniele.orler
 */
public class ExchangeTest {

    private Exchange exchange;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        exchange = new Exchange();
        String filePath = ClassLoader.getSystemClassLoader().getResource("exchange-info.json").getFile();
        ExchangeInfo info = objectMapper.readValue(new File(filePath), ExchangeInfo.class);
        exchange.setExchangeInfo(info);
    }

    @Test
    public void getLotFilter() throws Exception {

        Filters.LotFilter result = exchange.getLotFilter("ETHBTC");
        assertEquals(Filters.FilterType.LOT_SIZE, result.getFilterType());
    }

}