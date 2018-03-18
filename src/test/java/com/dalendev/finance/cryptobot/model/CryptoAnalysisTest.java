package com.dalendev.finance.cryptobot.model;

import com.dalendev.finance.cryptobot.services.ExponentialMovingAverageIndicator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author daniele.orler
 */
public class CryptoAnalysisTest {

    @Test
    public void toJson() throws Exception {
        CryptoAnalysis analysis = new ExponentialMovingAverageIndicator.EmaCryptoAnalysis(10);
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(analysis);

        assertEquals("{\"ema6h\":[],\"ema24h\":[],\"prices24\":[]}", result);
    }

}