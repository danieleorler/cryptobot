package com.dalendev.finance.cryptobot.adapters.rest.binance;

import com.dalendev.finance.cryptobot.model.Fill;
import com.dalendev.finance.cryptobot.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Instant;

/**
 * @author daniele.orler
 */

@Component
public class OrderAdapter extends BaseAdapter {

    @Autowired
    public OrderAdapter(RestTemplate restTemplate, ObjectMapper objectMapper) {
        super(restTemplate, objectMapper);
    }

    public Order placeOrder(Order order) throws IOException {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("symbol", order.getSymbol());
        body.add("side", order.getSide().name());
        body.add("type", order.getType().name());
        body.add("quantity", Double.toString(order.getQuantity()));
        body.add("timestamp", Long.toString(Instant.now().toEpochMilli()));
        body.add("recvWindow", Long.toString(100000));
        body.add("newOrderRespType", "FULL");

        RequestEntity<MultiValueMap<String, String>> request = RequestEntity
            .post(UriComponentsBuilder.fromHttpUrl(this.baseUrl + "/api/v3/order").build().toUri())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept(MediaType.APPLICATION_JSON)
            .header("X-MBX-APIKEY", this.apiKey)
            .body(sign(body));

        ResponseEntity<Order> response = restTemplate.exchange(request, Order.class);

        if(response.getBody().getSymbol() != null) {
            return response.getBody();
        }

        // in test mode binance doesn't return a body
        // so we just return the order passed to this method
        Fill fakeFill = new Fill(order.getPrice(), order.getQuantity());
        order.setStatus(Order.Status.FILLED);
        order.setId(-1L);
        order.getFills().add(fakeFill);
        return order;
    }

    public Order checkOrder(Order order) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("symbol", order.getSymbol());
        body.add("orderId", Long.toString(order.getId()));
        body.add("timestamp", Long.toString(Instant.now().toEpochMilli()));

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.baseUrl + "/api/v3/order");
        sign(body).toSingleValueMap().entrySet()
            .forEach(param -> builder.queryParam(param.getKey(), param.getValue()));

        RequestEntity<Void> request = RequestEntity
            .get(builder.build().encode().toUri())
            .accept(MediaType.APPLICATION_JSON)
            .header("X-MBX-APIKEY", this.apiKey)
            .build();

        ResponseEntity<Order> response = restTemplate.exchange(request, Order.class);

        return response.getBody();
    }

}
