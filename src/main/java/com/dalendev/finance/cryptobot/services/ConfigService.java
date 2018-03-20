package com.dalendev.finance.cryptobot.services;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

/**
 * Created by rein on 2018-01-24.
 */
@Service
public class ConfigService {
    private Log logger = LogFactory.getLog(ConfigService.class);

    @Value("${updatableConfig.takeProfit}")
    private int takeProfit;
    @Value("${updatableConfig.maxOrderPrice}")
    private double maxOrderPrice;
    @Value("${updatableConfig.maxPositionsOpened}")
    private int maxPositionsOpened;

    public void updateProperty(String key, String value)   {
        if(key.equalsIgnoreCase("takeProfit")) {
            takeProfit = parseInt(value);
            return;
        }
        if(key.equalsIgnoreCase("maxOrderPrice")) {
            maxOrderPrice = parseDouble(value);
            return;
        }
        if(key.equalsIgnoreCase("maxPositionsOpened")) {
            takeProfit = parseInt(value);
            return;
        }
    }

    public int getTakeProfit() {
        return takeProfit;
    }

    public double getMaxOrderPrice() {
        return maxOrderPrice;
    }

    public int getMaxPositionsOpened() {
        return maxPositionsOpened;
    }
}
