package com.dalendev.finance.cryptobot.adapters.rest.binance;

import com.dalendev.finance.cryptobot.model.binance.RestError;
import com.dalendev.finance.cryptobot.model.exceptions.RestRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * @author daniele.orler
 */


public class RestTemplateErrorHandler implements ResponseErrorHandler {

    private final ObjectMapper objectMapper;

    public RestTemplateErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        if(response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR ||
           response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
            return true;
        }
        return false;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        RestError error = objectMapper.readValue(toString(response.getBody()), RestError.class);
        throw new RestRequestException(error.getMsg());
    }

    private String toString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static void main(String[] args) {
        System.out.println(Math.log10(1));
    }
}
