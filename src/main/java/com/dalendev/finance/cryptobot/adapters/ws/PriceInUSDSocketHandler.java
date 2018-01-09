package com.dalendev.finance.cryptobot.adapters.ws;

import com.dalendev.finance.cryptobot.model.BTCPrice;
import com.dalendev.finance.cryptobot.model.KLine;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author daniele.orler
 */
public class PriceInUSDSocketHandler extends TextWebSocketHandler {

    protected Log logger = LogFactory.getLog(KLineSocketHandler.class);
    private final ObjectMapper objectMapper;
    private final BTCPrice btcPrice;

    @Autowired
    public PriceInUSDSocketHandler(ObjectMapper objectMapper, BTCPrice btcPrice) {
        this.objectMapper = objectMapper;
        this.btcPrice = btcPrice;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        KLine kLine = objectMapper.readValue(message.asBytes(), KLine.class);
        if(kLine.isComplete()) {
            btcPrice.setPriceinUSD(kLine.getClose());
        }
    }

}
