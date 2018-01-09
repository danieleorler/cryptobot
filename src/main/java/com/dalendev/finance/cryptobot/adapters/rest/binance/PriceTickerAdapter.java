package com.dalendev.finance.cryptobot.adapters.rest.binance;

import com.dalendev.finance.cryptobot.model.binance.PriceTicker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * @author daniele.orler
 */

@Component
public class PriceTickerAdapter extends BaseAdapter {

    private final RestTemplate restTemplate;

    @Autowired
    public PriceTickerAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<PriceTicker> getPriceTickers() throws URISyntaxException, IOException {

        RequestEntity<Void> request = RequestEntity.get(new URI(this.baseUrl + "/api/v3/ticker/price")).build();
        ResponseEntity<PriceTicker[]> response = restTemplate.exchange(request, PriceTicker[].class);

        return Arrays.asList(response.getBody());
    }
}
