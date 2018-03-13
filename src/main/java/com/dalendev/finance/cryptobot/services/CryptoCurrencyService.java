package com.dalendev.finance.cryptobot.services;

import com.dalendev.finance.cryptobot.model.CryptoCurrency;
import com.dalendev.finance.cryptobot.model.binance.PriceTicker;
import com.dalendev.finance.cryptobot.singletons.Market;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * @author daniele.orler
 */

@Service
public class CryptoCurrencyService {

    protected Log logger = LogFactory.getLog(CryptoCurrencyService.class);

    private final Market market;
    private final Indicator indicatorService;

    public CryptoCurrencyService(Market market, Indicator indicatorService) {
        this.market = market;
        this.indicatorService = indicatorService;
    }

    public void updateMarket(PriceTicker priceTicker) {

        if (market.getMarket().containsKey(priceTicker.getSymbol())) {
            CryptoCurrency crypto = market.getMarket().get(priceTicker.getSymbol());
            indicatorService.addSample(priceTicker.getPrice(), crypto);
            crypto.setLatestPrice(priceTicker.getPrice());
        } else {
            CryptoCurrency newCrypto = new CryptoCurrency(priceTicker.getSymbol());
            indicatorService.init(newCrypto);
            indicatorService.addSample(priceTicker.getPrice(), newCrypto);
            newCrypto.setLatestPrice(priceTicker.getPrice());
            market.getMarket().put(newCrypto.getSymbol(), newCrypto);
        }
    }

    public void setMarketReady() {
        market.setMarketReady(true);
        logger.debug("Market is ready");
    }

    public boolean isMarketReady() {
        return market.isMarketReady();
    }

}
