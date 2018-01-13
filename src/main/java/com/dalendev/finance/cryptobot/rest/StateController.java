package com.dalendev.finance.cryptobot.rest;

import com.dalendev.finance.cryptobot.singletons.Market;
import com.dalendev.finance.cryptobot.singletons.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author daniele.orler
 */

@RestController
@RequestMapping("/v1/api")
public class StateController {

    private final Market market;
    private final Portfolio portfolio;

    @Autowired
    public StateController(Market market, Portfolio portfolio) {
        this.market = market;
        this.portfolio = portfolio;
    }

    @RequestMapping(value = "/market", method = RequestMethod.GET)
    public Market getMarket() {
        return market;
    }

    @RequestMapping(value = "/portfolio", method = RequestMethod.GET)
    public Portfolio portfolio() {
        return portfolio;
    }

}
