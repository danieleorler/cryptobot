package com.dalendev.finance.cryptobot.adapters.ws;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author daniele.orler
 */
public class MarketDepthSocketHandler extends TextWebSocketHandler {

    protected Log logger = LogFactory.getLog(MarketDepthSocketHandler.class);


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        logger.info("Received: " + message);
    }

}
