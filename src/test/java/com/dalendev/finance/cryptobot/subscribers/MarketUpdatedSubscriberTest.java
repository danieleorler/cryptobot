package com.dalendev.finance.cryptobot.subscribers;

import com.dalendev.finance.cryptobot.adapters.rest.binance.OrderAdapter;
import com.dalendev.finance.cryptobot.model.CryptoCurrency;
import com.dalendev.finance.cryptobot.model.Order;
import com.dalendev.finance.cryptobot.model.events.OrderUpdatedEvent;
import com.dalendev.finance.cryptobot.model.events.OrdersSelectedEvent;
import com.dalendev.finance.cryptobot.services.ConfigService;
import com.dalendev.finance.cryptobot.services.ExponentialMovingAverageIndicator;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author daniele.orler
 */

@RunWith(MockitoJUnitRunner.class)
public class MarketUpdatedSubscriberTest {

    @Spy
    private Market market = new Market();
    @Spy
    private Portfolio portfolio = new Portfolio();
    @Spy
    private Counters counters = new Counters();
    @Mock
    private OrderAdapter orderAdapter;
    @Spy
    private TestEventBus eventBus = new TestEventBus();
    @Mock
    private Exchange exchange;
    @Mock
    private ConfigService configService;

    @InjectMocks
    private MarkedUpdatedSubscriber subscriber;

    private AtomicLong orderId = new AtomicLong(0);

    @Before
    public void setUp() throws Exception {
        eventBus.getOrders().clear();
        market.getMarket().clear();
    }

    @Test
    public void placeOrderTest() throws Exception {

        // mock order adapter
        Order orderFilled = TestUtil.jsonFileToObject("full-order-result-one-fill.json", Order.class);
        orderFilled.setPrice(0.01768200);
        when(orderAdapter.placeOrder(any(Order.class))).thenReturn(orderFilled);

        ExponentialMovingAverageIndicator indicator = new ExponentialMovingAverageIndicator(configService);
        CryptoCurrency currency = new CryptoCurrency("DGDBTC");
        indicator.init(currency);
        indicator.addSample(0.01767400, currency);
        market.getMarket().put("DGDBTC", currency);

        Order orderToBePlaced = new Order.Builder()
            .symbol("DGDBTC")
            .side(Order.Side.BUY)
            .type(Order.Type.MARKET)
            .quantity(0.057)
            .status(Order.Status.TO_BE_PLACED)
            .build();

        OrdersSelectedEvent ordersSelectedEvent = new OrdersSelectedEvent();
        ordersSelectedEvent.getOrders().addAll(Collections.singletonList(orderToBePlaced));
        subscriber.placeOrders(ordersSelectedEvent);

        Order result = eventBus.getOrders().get(0);

        assertEquals(0.01768200, result.getPrice(), 0.000001);
    }

    private static class TestEventBus extends EventBus {

        private final List<Order> orders;

        public TestEventBus() {
            orders = new ArrayList<>();
        }

        @Override
        public void register(Object object) {
            super.register(object);
            System.out.println("Registered");
        }

        @Override
        public void post(Object event) {

            if(event instanceof OrdersSelectedEvent) {
                this.orders.addAll(((OrdersSelectedEvent) event).getOrders());
            }

            if(event instanceof OrderUpdatedEvent) {
                OrderUpdatedEvent orderEvent = (OrderUpdatedEvent) event;
                this.orders.add(orderEvent.getOrder());
            }

            super.post(event);
        }

        public List<Order> getOrders() {
            return orders;
        }
    }

}