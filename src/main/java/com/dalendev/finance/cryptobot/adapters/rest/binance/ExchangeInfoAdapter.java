package com.dalendev.finance.cryptobot.adapters.rest.binance;

import com.dalendev.finance.cryptobot.model.binance.exchange.ExchangeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author daniele.orler
 */
@Component
public class ExchangeInfoAdapter extends BaseAdapter {

    private Log logger = LogFactory.getLog(ExchangeInfoAdapter.class);

    @Autowired
    public ExchangeInfoAdapter(RestTemplate restTemplate, ObjectMapper objectMapper) {
        super(restTemplate, objectMapper);
    }


    public ExchangeInfo getExchangeInfo() {

        RequestEntity<Void> request = RequestEntity
            .get(UriComponentsBuilder.fromHttpUrl(this.baseUrl + "/api/v1/exchangeInfo").build().toUri())
            .build();
        ResponseEntity<ExchangeInfo> response = restTemplate.exchange(request, ExchangeInfo.class);

        return response.getBody();
    }

    public List<Double> importSymbolPriceHistory(String symbol) {

        logger.debug("Importing prices for " + symbol);

        LocalDateTime from = LocalDateTime.now().minusHours(25);
        LocalDateTime stop = LocalDateTime.now().plusHours(1);
        LocalDateTime to = from.plusMinutes(500);
        List<Double> prices = new ArrayList<>();

        while(to.isBefore(stop)) {
            List<Double> list = getPricesForPeriod(
                    symbol,
                    from.toInstant(ZoneOffset.UTC).toEpochMilli(),
                    to.toInstant(ZoneOffset.UTC).toEpochMilli());

            prices.addAll(list);
            from = from.plusMinutes(500);
            to = to.plusMinutes(500);
        }

        return prices;
    }

    public List<Double> getPricesForPeriod(String symbol, Long from, Long to) {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("symbol", symbol);
        body.add("interval", "1m");
        body.add("startTime", from.toString());
        body.add("endTime", to.toString());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.baseUrl + "/api/v1/klines");
        body.toSingleValueMap().entrySet()
                .forEach(param -> builder.queryParam(param.getKey(), param.getValue()));

        RequestEntity<Void> request = RequestEntity
                .get(builder.build().encode().toUri())
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<JsonNode[]> response = restTemplate.exchange(request, JsonNode[].class);

        return Arrays.stream(response.getBody())
                .map(jsonNode -> jsonNode.get(4).asDouble())
                .collect(Collectors.toList());
    }

}
