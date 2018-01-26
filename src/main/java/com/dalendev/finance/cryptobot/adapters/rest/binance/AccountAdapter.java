package com.dalendev.finance.cryptobot.adapters.rest.binance;

import com.dalendev.finance.cryptobot.model.binance.AccountInformation;
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

import java.time.Instant;

/**
 * @author daniele.orler
 */
@Component
public class AccountAdapter extends BaseAdapter {

    @Autowired
    public AccountAdapter(RestTemplate restTemplate, ObjectMapper objectMapper) {
        super(restTemplate, objectMapper);
    }

    public AccountInformation getAccountInformation() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("timestamp", Long.toString(Instant.now().toEpochMilli()));
        body.add("recvWindow", Long.toString(100000));

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.baseUrl + "/api/v3/account");
        sign(body).toSingleValueMap().forEach((key, value) -> builder.queryParam(key, value));

        RequestEntity<Void> request = RequestEntity
            .get(builder.build().encode().toUri())
            .accept(MediaType.APPLICATION_JSON)
            .header("X-MBX-APIKEY", this.apiKey)
            .build();

        ResponseEntity<AccountInformation> response = restTemplate.exchange(request, AccountInformation.class);

        return response.getBody();
    }

}
