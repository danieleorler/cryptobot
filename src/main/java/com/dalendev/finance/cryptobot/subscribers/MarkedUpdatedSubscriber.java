package com.dalendev.finance.cryptobot.subscribers;

import com.dalendev.finance.cryptobot.adapters.rest.binance.OrderAdapter;
import com.dalendev.finance.cryptobot.model.*;
import com.dalendev.finance.cryptobot.model.events.MarketUpdatedEvent;
import com.dalendev.finance.cryptobot.model.events.PositionsCreatedEvent;
import com.dalendev.finance.cryptobot.singletons.Counters;
import com.dalendev.finance.cryptobot.singletons.Market;
import com.dalendev.finance.cryptobot.singletons.Portfolio;
import com.dalendev.finance.cryptobot.util.PriceUtil;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author daniele.orler
 */

@Component
public class MarkedUpdatedSubscriber {

    private Log logger = LogFactory.getLog(MarkedUpdatedSubscriber.class);

    private final Market market;
    private final Portfolio portfolio;
    private final EventBus eventBus;
    private final Counters counters;
    private final OrderAdapter orderAdapter;

    @Autowired
    public MarkedUpdatedSubscriber(Market market, EventBus eventBus, Portfolio portfolio, Counters counters, OrderAdapter orderAdapter) {
        this.market = market;
        this.portfolio = portfolio;
        this.eventBus = eventBus;
        this.counters = counters;
        this.orderAdapter = orderAdapter;
        eventBus.register(this);
    }

    @Subscribe
    public void updateMarket(MarketUpdatedEvent event) {
        this.market.getMarket().entrySet().stream()
            .map(Map.Entry::getValue)
            .filter(c -> c.getChange() > 1)
            .sorted((c1, c2) -> Float.compare(c2.getChange(), c1.getChange()))
            .forEach(c -> {
                if(!portfolio.getPortfolio().containsKey(c.getSymbol())) {
                    Float quantity = PriceUtil.adjust(0.002f / c.getLatestPrice(), 0.001f);
                    try {
                        orderAdapter.placeOrder(c.getSymbol(), "BUY", "MARKET", quantity);
                        logger.debug(String.format("Placed order for %.8f %s at %.8f", quantity, c.getSymbol(), c.getLatestPrice()));
                    } catch (Exception e) {
                        logger.error(String.format("Error placing an order for %.8f %s at %.8f", quantity, c.getSymbol(), c.getLatestPrice()));
                        logger.error(e);
                    }

                }
            });

        eventBus.post(new PositionsCreatedEvent());
    }

    @Subscribe
    public void updatePositions(PositionsCreatedEvent event) {
        this.portfolio.getPortfolio().entrySet().stream()
            .map(Map.Entry::getValue)
            .forEach(position -> {
                CryptoCurrency crypto = market.getMarket().get(position.getSymbol());
                Float currentPrice = crypto.getLatestPrice();
                Float openPrice = position.getOpenPrice();
                position.setChange(PriceUtil.change(openPrice, currentPrice));
                System.out.println(String.format("Position on %s: start %.8f now %.8f -> %.4f%%",
                        position.getSymbol(),
                        position.getOpenPrice(),
                        crypto.getLatestPrice(),
                        position.getChange()));
            });

        this.portfolio.getPortfolio().entrySet()
            .removeIf(entry ->  {
                Position position = entry.getValue();
                if(shouldClosePosition(position, 5f)) {
                    logger.debug(String.format("Closed position on %s with result %.4f", position.getSymbol(), position.getChange()));
                    Float finalPrice = PriceUtil.addPercentage(position.getOpenPrice(), position.getChange());
                    counters.addToProfit(finalPrice - position.getOpenPrice());
                    System.out.println("Profit balance: " + counters.getProfit() + " BTC");
                    return true;
                }
                return false;
            });
    }


    private Boolean shouldClosePosition(Position position, Float gain) {

        if(position.getChange() > gain) {
            return true;
        }
        return false;
    }

}
