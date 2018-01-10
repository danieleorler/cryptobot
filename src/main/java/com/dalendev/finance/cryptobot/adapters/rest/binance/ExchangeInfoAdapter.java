package com.dalendev.finance.cryptobot.adapters.rest.binance;

import com.dalendev.finance.cryptobot.model.binance.exchange.ExchangeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author daniele.orler
 */
@Component
public class ExchangeInfoAdapter extends BaseAdapter {

    @Autowired
    public ExchangeInfoAdapter(RestTemplate restTemplate) {
        super(restTemplate);
    }


    public ExchangeInfo getExchangeInfo() throws URISyntaxException, IOException {

        RequestEntity<Void> request = RequestEntity
            .get(new URI(this.baseUrl + "/api/v1/exchangeInfo"))
            .build();
        ResponseEntity<ExchangeInfo> response = restTemplate.exchange(request, ExchangeInfo.class);

        return response.getBody();
    }

}
