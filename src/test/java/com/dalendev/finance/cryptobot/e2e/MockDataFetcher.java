package com.dalendev.finance.cryptobot.e2e;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author daniele.orler
 */
public class MockDataFetcher {


    public static void main(String[] args) {

        LocalDateTime from = LocalDateTime.now().minusHours(96);
        LocalDateTime stop = LocalDateTime.now().minusHours(46);
        LocalDateTime to = from.plusMinutes(500);
        int c = 1;
        String path = "/cryptobot/src/test/resources/e2e/dgdbtc/";

        while(from.isBefore(stop)) {
            String klines = getPricesForPeriod(
                    "DGDBTC",
                    from.toInstant(ZoneOffset.UTC).toEpochMilli(),
                    to.toInstant(ZoneOffset.UTC).toEpochMilli());

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(String.format(path+"dgdbtc-import-price-history-%d.json", c++)));
                writer.write(klines);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(from);
            System.out.println(to);
            System.out.println("------");
            from = from.plusMinutes(500);
            to = to.plusMinutes(500);
        }

        System.out.println("*****");

        List<Double> futurePrices = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        while(from.isBefore(LocalDateTime.now())) {
            System.out.println(from);
            System.out.println(to);
            System.out.println("------");
            String klines = getPricesForPeriod(
                    "DGDBTC",
                    from.toInstant(ZoneOffset.UTC).toEpochMilli(),
                    to.toInstant(ZoneOffset.UTC).toEpochMilli());

            try {
                JsonNode[] jsonNodes = objectMapper.readValue(klines, JsonNode[].class);
                Arrays.stream(jsonNodes)
                    .map(jsonNode -> jsonNode.get(4).asDouble())
                    .forEach(price -> futurePrices.add(price));
            } catch (IOException e) {
                e.printStackTrace();
            }

            from = from.plusMinutes(500);
            to = to.plusMinutes(500);
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path+"dgdbtc-prices.json"));
            writer.write(objectMapper.writeValueAsString(futurePrices));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String getPricesForPeriod(String symbol, Long from, Long to) {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("symbol", symbol);
        body.add("interval", "1m");
        body.add("startTime", from.toString());
        body.add("endTime", to.toString());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.binance.com/api/v1/klines");
        body.toSingleValueMap().entrySet()
                .forEach(param -> builder.queryParam(param.getKey(), param.getValue()));

        RequestEntity<Void> request = RequestEntity
                .get(builder.build().encode().toUri())
                .accept(MediaType.APPLICATION_JSON)
                .build();

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        return response.getBody();
    }

}
