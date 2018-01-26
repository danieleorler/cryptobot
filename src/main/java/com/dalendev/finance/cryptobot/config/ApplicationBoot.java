package com.dalendev.finance.cryptobot.config;

import com.dalendev.finance.cryptobot.adapters.rest.binance.AccountAdapter;
import com.dalendev.finance.cryptobot.adapters.rest.binance.ExchangeInfoAdapter;
import com.dalendev.finance.cryptobot.model.binance.AccountInformation;
import com.dalendev.finance.cryptobot.model.binance.PriceTicker;
import com.dalendev.finance.cryptobot.model.binance.exchange.ExchangeInfo;
import com.dalendev.finance.cryptobot.model.binance.exchange.Symbol;
import com.dalendev.finance.cryptobot.model.exceptions.LowBalanceException;
import com.dalendev.finance.cryptobot.services.CryptoCurrencyService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author daniele.orler
 */
@Component
public class ApplicationBoot implements ApplicationListener<ApplicationReadyEvent> {

    private Log logger = LogFactory.getLog(CryptoCurrencyService.class);

    private final ExchangeInfoAdapter exchangeAdapter;
    private final CryptoCurrencyService currencyService;
    private final AccountAdapter accountAdapter;
    private final Integer maxExchangeSymbols;

    @Autowired
    public ApplicationBoot(
            ExchangeInfoAdapter exchangeAdapter,
            CryptoCurrencyService currencyService,
            AccountAdapter accountAdapter,
            @Value("${maxExchangeSymbols:99999}") Integer maxExchangeSymbols) {
        this.exchangeAdapter = exchangeAdapter;
        this.currencyService = currencyService;
        this.accountAdapter = accountAdapter;
        this.maxExchangeSymbols = maxExchangeSymbols;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent applicationEvent) {
        assertEnoughBNB();
        importSymbols();
    }

    private void importSymbols() {
        ExchangeInfo exchangeInfo = exchangeAdapter.getExchangeInfo();

        exchangeInfo.getSymbols().stream().parallel()
                .map(Symbol::getSymbol)
                .sorted()
                .filter(symbol -> symbol.endsWith("BTC"))
                .limit(maxExchangeSymbols)
                .forEach(symbol -> exchangeAdapter.importSymbolPriceHistory(symbol).stream()
                        .map(price -> new PriceTicker(symbol, price))
                        .forEach(currencyService::updateMarket)
                );

        currencyService.setMarketReady();
    }

    private void assertEnoughBNB() {
        AccountInformation accountInformation = accountAdapter.getAccountInformation();
        Double bnbBalance = accountInformation.getBalances().stream()
                .filter(balance -> balance.getAsset().equalsIgnoreCase("BNB"))
                .findFirst()
                .orElseThrow(() -> new LowBalanceException("BNB balance 0, buy some before starting the bot"))
                .getFree();

        logger.warn(String.format("You current BNB balance is: %.8f, make sure is enough for trading",
            bnbBalance));
    }

}
