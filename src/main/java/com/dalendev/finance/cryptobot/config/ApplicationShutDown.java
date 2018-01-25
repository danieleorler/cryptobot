package com.dalendev.finance.cryptobot.config;

import com.dalendev.finance.cryptobot.adapters.rest.binance.OrderAdapter;
import com.dalendev.finance.cryptobot.model.Order;
import com.dalendev.finance.cryptobot.singletons.Portfolio;
import com.dalendev.finance.cryptobot.util.PriceUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Map;

/**
 * @author daniele.orler
 */
@Component
public class ApplicationShutDown {

    private Log logger = LogFactory.getLog(ApplicationShutDown.class);

    private final Portfolio portfolio;
    private final OrderAdapter adapter;

    @Autowired
    public ApplicationShutDown(Portfolio portfolio, OrderAdapter adapter) {
        this.portfolio = portfolio;
        this.adapter = adapter;
    }

    @PreDestroy
    public void sellEverything() {
        logger.info("The bot is going down, I'm selling everything");

        portfolio.getPositions().entrySet().stream()
            .map(Map.Entry::getValue)
            .map(p -> new Order.Builder()
                .symbol(p.getSymbol())
                .side(Order.Side.SELL)
                .type(Order.Type.MARKET)
                .quantity(p.getAmount())
                .price(PriceUtil.addPercentage(p.getOpenPrice(), p.getChange()))
                .status(Order.Status.TO_BE_PLACED)
                .build())
            .forEach(o -> {
                Order order = adapter.placeOrder(o);
                logger.debug(order);
            });
    }
}
