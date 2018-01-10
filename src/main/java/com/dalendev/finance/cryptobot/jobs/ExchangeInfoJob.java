package com.dalendev.finance.cryptobot.jobs;

import com.dalendev.finance.cryptobot.adapters.rest.binance.ExchangeInfoAdapter;
import com.dalendev.finance.cryptobot.model.binance.exchange.ExchangeInfo;
import com.dalendev.finance.cryptobot.singletons.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author daniele.orler
 */

@Component
public class ExchangeInfoJob {

    private Log logger = LogFactory.getLog(PriceTickerJob.class);

    private final Exchange exchange;
    private final ExchangeInfoAdapter adapter;

    @Autowired
    public ExchangeInfoJob(Exchange exchange, ExchangeInfoAdapter adapter) {
        this.exchange = exchange;
        this.adapter = adapter;
    }

    @Scheduled(fixedDelay = 12*60*60*1000)
    public void updateExchangeInfo() {
        try {
            logger.debug("Updating exchange info");
            ExchangeInfo info = adapter.getExchangeInfo();
            exchange.setExchangeInfo(info);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
