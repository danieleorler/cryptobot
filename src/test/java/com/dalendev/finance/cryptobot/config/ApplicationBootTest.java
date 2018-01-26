package com.dalendev.finance.cryptobot.config;

import com.dalendev.finance.cryptobot.adapters.rest.binance.AccountAdapter;
import com.dalendev.finance.cryptobot.adapters.rest.binance.ExchangeInfoAdapter;
import com.dalendev.finance.cryptobot.model.binance.AccountInformation;
import com.dalendev.finance.cryptobot.model.binance.Balance;
import com.dalendev.finance.cryptobot.model.binance.exchange.ExchangeInfo;
import com.dalendev.finance.cryptobot.model.exceptions.LowBalanceException;
import com.dalendev.finance.cryptobot.services.CryptoCurrencyService;
import com.dalendev.finance.cryptobot.test.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

/**
 * @author daniele.orler
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplicationBootTest {

    @Mock
    private ExchangeInfoAdapter exchangeAdapter;
    @Mock
    private CryptoCurrencyService currencyService;
    @Mock
    private AccountAdapter accountAdapter;

    private ApplicationBoot applicationBoot;
    private ApplicationReadyEvent event;

    @Before
    public void setUp() throws Exception {
        applicationBoot = new ApplicationBoot(exchangeAdapter, currencyService, accountAdapter, 1);
        event = new ApplicationReadyEvent(new SpringApplication(), null, null);
    }

    @Test(expected = LowBalanceException.class)
    public void assertBNBBalanceFailTest() {
        AccountInformation accountInformation = TestUtil.jsonFileToObject("account-information.json", AccountInformation.class);
        when(accountAdapter.getAccountInformation()).thenReturn(accountInformation);
        applicationBoot.onApplicationEvent(event);
    }

    @Test
    public void assertBNBBalancePassTest() {
        AccountInformation accountInformation = TestUtil.jsonFileToObject("account-information.json", AccountInformation.class);
        accountInformation.getBalances()
            .add(new Balance("BNB", 1.0, 0.0));

        ExchangeInfo exchangeInfo = new ExchangeInfo();
        exchangeInfo.setSymbols(new ArrayList<>());

        when(accountAdapter.getAccountInformation()).thenReturn(accountInformation);
        when(exchangeAdapter.getExchangeInfo()).thenReturn(exchangeInfo);
        applicationBoot.onApplicationEvent(event);
    }


}