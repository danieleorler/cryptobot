package com.dalendev.finance.cryptobot.services;

import com.dalendev.finance.cryptobot.model.CryptoCurrency;
import com.dalendev.finance.cryptobot.singletons.Market;
import com.dalendev.finance.cryptobot.model.binance.PriceTicker;
import com.dalendev.finance.cryptobot.util.PriceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author daniele.orler
 */

@Service
public class CryptoCurrencyService {

    private final Market market;

    @Autowired
    public CryptoCurrencyService(Market market) {
        this.market = market;
    }

    public void updateMarket(PriceTicker priceTicker) {

        if (market.getMarket().containsKey(priceTicker.getSymbol())) {
            updatePriceHistory(priceTicker);
        } else {
            CryptoCurrency newCrypto = new CryptoCurrency(priceTicker.getSymbol());
            newCrypto.getPricePoints().add(priceTicker.getPrice());
            newCrypto.setLatestPrice(priceTicker.getPrice());
            market.getMarket().put(newCrypto.getSymbol(), newCrypto);
        }
    }

    private void updatePriceHistory(PriceTicker priceTicker) {

        CryptoCurrency crypto = market.getMarket().get(priceTicker.getSymbol());

        Float oldestPrice = crypto.getPricePoints().peek();
        Float newestPrice = priceTicker.getPrice();
        crypto.getPricePoints().add(newestPrice);
        crypto.setLatestPrice(priceTicker.getPrice());

        if(oldestPrice == null) {
            return;
        }

        crypto.setChange(PriceUtil.change(oldestPrice, newestPrice));
    }

}
