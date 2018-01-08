package com.dalendev.finance.cryptobot;

import com.dalendev.finance.cryptobot.adapters.ws.KLineSocketHandler;
import com.dalendev.finance.cryptobot.adapters.ws.PriceInUSDSocketHandler;
import com.dalendev.finance.cryptobot.model.BTCPrice;
import com.dalendev.finance.cryptobot.model.MarketsStatus;
import com.dalendev.finance.cryptobot.model.Symbol;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author daniele.orler
 */
@SpringBootApplication
public class App {

    @Autowired
    private KLineSocketHandler kLineSocketHandler;
    @Autowired
    PriceInUSDSocketHandler priceInUSDSocketHandler;

    public static void main(final String[] args) {
        SpringApplication.run(App.class, args);
    }


    @Bean
    public List<WebSocketConnectionManager> wsConnectionManager(WebSocketClient client) {

        return Arrays.stream(Symbol.values())
            .map(symbol -> startWSConnection(client, symbol))
            .collect(Collectors.toList());

    }

    @Bean
    public StandardWebSocketClient client() {
        return new StandardWebSocketClient();
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    MarketsStatus marketsStatus() {
        return new MarketsStatus();
    }

    @Bean
    BTCPrice btcPrice() {
        return new BTCPrice();
    }

    private WebSocketConnectionManager startWSConnection(WebSocketClient client, Symbol symbol) {

        TextWebSocketHandler handler;

        if(symbol.isBasePrice()) {
            handler = priceInUSDSocketHandler;
        } else {
            handler = kLineSocketHandler;
        }

        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                client,
                handler,
                "wss://stream.binance.com:9443/ws/"+symbol.getTopic()+"@kline_1m"
        );
        manager.setAutoStartup(true);
        manager.start();

        return manager;
    }

}
