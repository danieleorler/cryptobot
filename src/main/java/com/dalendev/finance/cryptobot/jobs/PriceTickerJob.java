package com.dalendev.finance.cryptobot.jobs;

import com.dalendev.finance.cryptobot.adapters.rest.binance.PriceTickerAdapter;
import com.dalendev.finance.cryptobot.model.binance.PriceTicker;
import com.dalendev.finance.cryptobot.model.events.MarketUpdatedEvent;
import com.dalendev.finance.cryptobot.services.CryptoCurrencyService;
import com.google.common.eventbus.EventBus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author daniele.orler
 */

@Component
public class PriceTickerJob {

    protected Log logger = LogFactory.getLog(PriceTickerJob.class);

    private final PriceTickerAdapter priceTickerAdapter;
    private final CryptoCurrencyService cryptoCurrencyService;
    private final EventBus eventBus;

    @Autowired
    public PriceTickerJob(PriceTickerAdapter priceTickerAdapter, CryptoCurrencyService cryptoCurrencyService, EventBus eventBus) {
        this.priceTickerAdapter = priceTickerAdapter;
        this.cryptoCurrencyService = cryptoCurrencyService;
        this.eventBus = eventBus;
    }

    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    public void samplePrice() {
        if(cryptoCurrencyService.isMarketReady()) {
            sampleMarket();
        }
    }

    private void sampleMarket() {
        try {
            List<PriceTicker> priceTickers = this.priceTickerAdapter.getPriceTickers();

            priceTickers.stream()
                .filter(priceTicker -> priceTicker.getSymbol().endsWith("BTC"))
                .forEach(priceTicker -> cryptoCurrencyService.updateMarket(priceTicker));

            eventBus.post(new MarketUpdatedEvent());

        } catch (Exception e) {
            logger.error(e);
        }
    }

}
