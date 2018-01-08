package com.dalendev.finance.cryptobot.adapters.rest.binance;

import com.dalendev.finance.cryptobot.model.binance.PriceTickers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author daniele.orler
 */
public class PriceTickerAdapter extends BaseAdapter {

    private final RestTemplate restTemplate;

    @Autowired
    public PriceTickerAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PriceTickers getBookTickers() throws URISyntaxException {

        RequestEntity<Void> request = RequestEntity.get(new URI(this.baseUrl + "/api/v3/ticker/price")).build();
        ResponseEntity<PriceTickers> response = restTemplate.exchange(request, PriceTickers.class);

        return response.getBody();
    }
}
