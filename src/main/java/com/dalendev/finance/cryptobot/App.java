package com.dalendev.finance.cryptobot;

import com.dalendev.finance.cryptobot.model.Counters;
import com.dalendev.finance.cryptobot.model.Market;
import com.dalendev.finance.cryptobot.model.Portfolio;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executors;

/**
 * @author daniele.orler
 */
@SpringBootApplication
@EnableScheduling
public class App {

    public static void main(final String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public EventBus eventBus() {
        return new AsyncEventBus(Executors.newCachedThreadPool());
    }

    @Bean
    Market marketsStatus() {
        return new Market();
    }

    @Bean
    Portfolio portfolio() {
        return new Portfolio();
    }

    @Bean
    Counters counters() {
        return new Counters();
    }

}
