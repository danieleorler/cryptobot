package com.dalendev.finance.cryptobot.e2e;

import com.dalendev.finance.cryptobot.model.Order;
import com.dalendev.finance.cryptobot.model.binance.PriceTicker;
import com.dalendev.finance.cryptobot.model.binance.exchange.ExchangeInfo;
import com.dalendev.finance.cryptobot.model.binance.exchange.Filters;
import com.dalendev.finance.cryptobot.model.binance.exchange.Symbol;
import com.dalendev.finance.cryptobot.test.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.mock.action.ExpectationCallback;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * @author daniele.orler
 */
public class BinanceMock {

    private static ClientAndServer mockServer;
    private static Queue<PriceTicker> responses;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static AtomicLong orderId = new AtomicLong();
    private static AtomicLong priceHistoryCounter = new AtomicLong();
    private static Map<Long, Order> orders = new HashMap<>();

    public static void main(String[] args) {
        Double[] prices = TestUtil.jsonFileToObject("ETC.json", Double[].class);

        responses = new ArrayBlockingQueue<>(prices.length);
        Arrays.stream(prices)
                .map(price -> new PriceTicker("ETCBTC", price))
                .forEach(pt -> responses.add(pt));

        mockServer = startClientAndServer(8123);

        mockServer.when(request()
                .withMethod("GET")
                .withPath("/api/v3/ticker/price"))
                .callback(new PriceTickerCallback());
        mockServer.when(request()
                .withMethod("POST")
                .withPath("/api/v3/order/test"))
                .callback(new PlaceOrderCallback());
        mockServer.when(request()
                .withMethod("GET")
                .withPath("/api/v3/order"))
                .callback(new CheckOrderCallback());
        mockServer.when(request()
                .withMethod("GET")
                .withPath("/api/v1/exchangeInfo"))
                .callback(new ExchangeInfoCallback());
        mockServer.when(request()
                .withMethod("GET")
                .withPath("/api/v1/klines"))
                .callback(new PriceHistoryCallback());
    }

    public static class PriceTickerCallback implements ExpectationCallback {

        @Override
        public HttpResponse handle(HttpRequest httpRequest) {
            try {
                return response()
                    .withStatusCode(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(objectMapper.writeValueAsString(Collections.singleton(responses.remove())));
            } catch (Exception e) {
                return response()
                        .withStatusCode(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"code\": -1, \"msg\": \"" + e.getMessage() + "\"}");
            }
        }
    }

    public static class PlaceOrderCallback implements ExpectationCallback {

        @Override
        public HttpResponse handle(HttpRequest httpRequest) {
            try {
                String uri = "http://localhost:8123?"+httpRequest.getBodyAsString();
                Map<String, String> parameters =
                        UriComponentsBuilder.fromUriString(uri).build().getQueryParams().toSingleValueMap();

                Order order = new Order.Builder()
                    .symbol(parameters.get("symbol"))
                    .side(Order.Side.valueOf(parameters.get("side")))
                    .type(Order.Type.valueOf(parameters.get("type")))
                    .quantity(Double.parseDouble(parameters.get("quantity")))
                    .price(responses.element().getPrice())
                    .build();
                order.setId(orderId.incrementAndGet());

                orders.put(order.getId(), order);

                return response()
                    .withStatusCode(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(objectMapper.writeValueAsString(order));
            } catch (Exception e) {
                return response()
                        .withStatusCode(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"code\": -1, \"msg\": \"" + e.getMessage() + "\"}");
            }
        }
    }

    private static class ExchangeInfoCallback implements ExpectationCallback {

        @Override
        public HttpResponse handle(HttpRequest httpRequest) {
            try {
                ExchangeInfo info = new ExchangeInfo();
                Symbol symbol = new Symbol();
                symbol.setSymbol("ETCBTC");
                Filters.LotFilter filter = new Filters.LotFilter();
                filter.setMaxQuantity(0.01);
                filter.setFilterType(Filters.FilterType.LOT_SIZE);
                filter.setMinQuantity(10000000.0);
                filter.setStepSize(0.01);
                symbol.setFilters(Arrays.asList(filter));
                info.setSymbols(Arrays.asList(symbol));
                return response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(info));
            } catch (Exception e) {
                return response()
                        .withStatusCode(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"code\": -1, \"msg\": \"" + e.getMessage() + "\"}");
            }
        }
    }

    private static class CheckOrderCallback implements ExpectationCallback {
        @Override
        public HttpResponse handle(HttpRequest httpRequest) {
            try {
                String orderId = httpRequest.getFirstQueryStringParameter("orderId");

                Order order = orders.get(Long.parseLong(orderId));
                order.setStatus(Order.Status.FILLED);

                return response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(order));
            } catch (Exception e) {
                return response()
                        .withStatusCode(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"code\": -1, \"msg\": \"" + e.getMessage() + "\"}");
            }
        }
    }

    private static class PriceHistoryCallback implements ExpectationCallback {
        @Override
        public HttpResponse handle(HttpRequest httpRequest) {
            try {

                String file = String.format("etcbtc/etcbtc-import-price-history-%d.json", priceHistoryCounter.incrementAndGet());

                return response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(TestUtil.jsonFileToString(file));
            } catch (Exception e) {
                return response()
                        .withStatusCode(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"code\": -1, \"msg\": \"" + e.getMessage() + "\"}");
            }
        }
    }
}
