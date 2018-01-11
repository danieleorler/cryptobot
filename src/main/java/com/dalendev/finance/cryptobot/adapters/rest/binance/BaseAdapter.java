package com.dalendev.finance.cryptobot.adapters.rest.binance;

import com.dalendev.finance.cryptobot.model.exceptions.EncodingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.stream.Collectors;

/**
 * @author daniele.orler
 */
public class BaseAdapter {

    protected final String baseUrl = "https://api.binance.com";
    protected final RestTemplate restTemplate;
    protected final ObjectMapper objectMapper;

    @Value("${binance.api_key}")
    protected String apiKey;

    @Value("${binance.api_secret}")
    private String apiSecret;

    @Autowired
    public BaseAdapter(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    protected MultiValueMap<String, String> sign(MultiValueMap<String, String> params) {

        String mergedParams = params.toSingleValueMap().entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining("&"));

        params.add("signature", encode(apiSecret, mergedParams));

        return params;
    }

    private String encode(String key, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
        } catch(Exception e) {
            throw new EncodingException(e);
        }
    }
}
