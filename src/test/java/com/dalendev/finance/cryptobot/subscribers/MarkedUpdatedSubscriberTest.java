package com.dalendev.finance.cryptobot.subscribers;

import com.dalendev.finance.cryptobot.adapters.rest.binance.OrderAdapter;
import com.dalendev.finance.cryptobot.model.CryptoCurrency;
import com.dalendev.finance.cryptobot.model.Order;
import com.dalendev.finance.cryptobot.model.binance.PriceTicker;
import com.dalendev.finance.cryptobot.model.binance.exchange.Filters;
import com.dalendev.finance.cryptobot.model.events.MarketUpdatedEvent;
import com.dalendev.finance.cryptobot.model.events.OrderUpdatedEvent;
import com.dalendev.finance.cryptobot.model.events.OrdersSelectedEvent;
import com.dalendev.finance.cryptobot.singletons.Counters;
import com.dalendev.finance.cryptobot.singletons.Exchange;
import com.dalendev.finance.cryptobot.singletons.Market;
import com.dalendev.finance.cryptobot.singletons.Portfolio;
import com.dalendev.finance.cryptobot.test.TestUtil;
import com.google.common.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author daniele.orler
 */

@RunWith(MockitoJUnitRunner.class)
public class MarkedUpdatedSubscriberTest {

    @Spy
    private Market market = new Market();
    @Spy
    private Portfolio portfolio = new Portfolio();
    @Spy
    private Counters counters = new Counters();
    @Mock
    private OrderAdapter orderAdapter;
    @Spy
    private TestEventBus eventBus;
    @Mock
    private Exchange exchange;

    @InjectMocks
    private MarkedUpdatedSubscriber subscriber;

    private AtomicLong orderId = new AtomicLong(0);

    @Before
    public void setUp() throws IOException, URISyntaxException {

        Filters.LotFilter filter = new Filters.LotFilter();
        filter.setMaxQuantity(0.01);
        filter.setMinQuantity(10000000.0);
        filter.setStepSize(0.01);

        when(exchange.getLotFilter("ETCBTC"))
                .thenReturn(filter);

        when(orderAdapter.placeOrder(any(Order.class)))
                .thenReturn(orderId.getAndAdd(1));

        when(orderAdapter.checkOrder(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order argument = invocation.getArgument(0);
                    argument.setStatus(Order.Status.FILLED);
                    return argument;
                });
    }


    @Test
    public void selectBuyOrders() throws Exception {

        Double[] prices = TestUtil.jsonFileToObject("ETC.json", Double[].class);

        Arrays.stream(prices)
            .map(price -> new PriceTicker("ETCBTC", price))
            .forEach(pt -> {
                updateMarket(pt);
                //subscriber.selectBuyOrders(new PositionsUpdatedEvent());
            });

        System.out.println(eventBus.getOrders().size());

    }

    private void updateMarket(PriceTicker priceTicker) {

        if (market.getMarket().containsKey(priceTicker.getSymbol())) {
            CryptoCurrency crypto = market.getMarket().get(priceTicker.getSymbol());
            crypto.addPriceSample(priceTicker.getPrice());
            crypto.setLatestPrice(priceTicker.getPrice());
        } else {
            CryptoCurrency newCrypto = new CryptoCurrency(priceTicker.getSymbol());
            newCrypto.setLatestPrice(priceTicker.getPrice());
            market.getMarket().put(newCrypto.getSymbol(), newCrypto);
        }
        eventBus.post(new MarketUpdatedEvent());
    }

    private static class TestEventBus extends EventBus {

        private final List<Order> orders;

        public TestEventBus() {
            orders = new ArrayList<>();
        }

        @Override
        public void register(Object object) {
            super.register(object);
            System.out.println("REgistered");
        }

        @Override
        public void post(Object event) {

            if(event instanceof OrdersSelectedEvent) {
                this.orders.addAll(((OrdersSelectedEvent) event).getOrders());
            }

            if(event instanceof OrderUpdatedEvent) {
                OrderUpdatedEvent orderEvent = (OrderUpdatedEvent) event;
                if(orderEvent.getOrder().getStatus() == Order.Status.PLACED) {
                    orderEvent.getOrder().setStatus(Order.Status.FILLED);
                }
            }

            super.post(event);
        }

        public List<Order> getOrders() {
            return orders;
        }
    }



}