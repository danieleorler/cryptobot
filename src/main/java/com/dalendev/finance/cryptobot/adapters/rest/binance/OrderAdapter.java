package com.dalendev.finance.cryptobot.adapters.rest.binance;

import com.dalendev.finance.cryptobot.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;

/**
 * @author daniele.orler
 */

@Component
public class OrderAdapter extends BaseAdapter {

    @Autowired
    public OrderAdapter(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public String placeOrder(Order order) throws URISyntaxException, IOException {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("symbol", order.getSymbol());
        body.add("side", order.getSide().name());
        body.add("type", order.getType().name());
        body.add("quantity", Float.toString(order.getQuantity()));
        body.add("timestamp", Long.toString(Instant.now().toEpochMilli()));

        RequestEntity<MultiValueMap<String, String>> request = RequestEntity
            .post(new URI(this.baseUrl + "/api/v3/order/test"))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept(MediaType.APPLICATION_JSON)
            .header("X-MBX-APIKEY", this.apiKey)
            .body(sign(body));

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        return response.getBody();
    }

}
