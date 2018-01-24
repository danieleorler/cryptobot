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

    @RequestMapping(value = "/{key}", method = RequestMethod.POST)
    public void updateProperty(@PathVariable String key, @RequestBody String value) {
        configService.updateProperty(key, value);
    }
}
