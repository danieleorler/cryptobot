package com.dalendev.finance.cryptobot.rest;

import com.dalendev.finance.cryptobot.services.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/api/config")
@CrossOrigin(origins = "*")
public class ConfigController {


    private final ConfigService configService;

    @Autowired
    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, String> getAllProperties() {
        return configService.getUpdatableConfig();
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public String getProperty(@PathVariable String key) {
        return configService.getProperty(key);
    }

    /**
     * Ex: curl -X PUT http://localhost:8080/v1/api/config/takeProfit -d value=10
     * @param key name of the property to update
     * @param value the new value
     */
    @RequestMapping(value = "/{key}", method = RequestMethod.PUT)
    public void updateProperty(@PathVariable String key, @RequestParam String value) {
       configService.updateProperty(key, value);
    }
}
