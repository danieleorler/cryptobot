package com.dalendev.finance.cryptobot.services;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rein on 2018-01-24.
 */
@Service
@ConfigurationProperties()
public class ConfigService {
    private Log logger = LogFactory.getLog(ConfigService.class);

    private final Map<String, String> updatableConfig = new HashMap<>();

    @PostConstruct
    public void post() {
        logger.info("Started with default properties. " +  this);
    }

    public Map<String, String> getUpdatableConfig() {
        return updatableConfig;
    }

    @Override
    public String toString() {
        return "ConfigService{" +
                "config=" + updatableConfig +
                '}';
    }

    public void updateProperty(String key, String value)   {
        if(!updatableConfig.containsKey(key)){
            throw new IllegalArgumentException(key + " is not a supported property");
        }
        logger.info(String.format("Updating property %s to %s", key, value));
        updatableConfig.put(key, value);
    }

    public String getProperty(String key){
        return updatableConfig.get(key);
    }

    private Double getAsDouble(String key){
        return Double.parseDouble(updatableConfig.get(key));
    }

    public Double getTakeProfit() {
        return getAsDouble("takeProfit");
    }

    public Double getMaxOrderPrice() {
        return getAsDouble("maxOrderPrice");
    }

    public Integer getMaxOrderPositions() {
        return Integer.parseInt(getProperty("maxOrderPositions"));
    }
}
