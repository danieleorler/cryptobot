package com.dalendev.finance.cryptobot.config;

import com.dalendev.finance.cryptobot.adapters.rest.binance.ExchangeInfoAdapter;
import com.dalendev.finance.cryptobot.model.binance.PriceTicker;
import com.dalendev.finance.cryptobot.model.binance.exchange.ExchangeInfo;
import com.dalendev.finance.cryptobot.model.binance.exchange.Symbol;
import com.dalendev.finance.cryptobot.services.CryptoCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author daniele.orler
 */
@Component
public class ApplicationBoot implements ApplicationListener<ApplicationReadyEvent> {

    private final ExchangeInfoAdapter adapter;
    private final CryptoCurrencyService currencyService;

    @Autowired
    public ApplicationBoot(ExchangeInfoAdapter adapter, CryptoCurrencyService currencyService) {
        this.adapter = adapter;
        this.currencyService = currencyService;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent applicationEvent) {

        ExchangeInfo exchangeInfo = adapter.getExchangeInfo();

        exchangeInfo.getSymbols().stream().parallel()
            .map(Symbol::getSymbol)
            .sorted()
            .filter(symbol -> symbol.endsWith("BTC"))
            .forEach(symbol -> adapter.importSymbolPriceHistory(symbol).stream()
                    .map(price -> new PriceTicker(symbol, price))
                    .forEach(pt -> currencyService.updateMarket(pt))
            );

        currencyService.setMarketReady();
    }
}
