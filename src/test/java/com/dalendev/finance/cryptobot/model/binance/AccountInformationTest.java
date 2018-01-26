package com.dalendev.finance.cryptobot.model.binance;

import com.dalendev.finance.cryptobot.test.TestUtil;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * @author daniele.orler
 */
public class AccountInformationTest {

    @Test
    public void deserializationTest() {
        AccountInformation result = TestUtil.jsonFileToObject("account-information.json", AccountInformation.class);
        assertEquals(2, result.getBalances().size());
        assertEquals(4723846.89208129, result.getBalances().get(0).getFree(), 0.00000001);
    }

}