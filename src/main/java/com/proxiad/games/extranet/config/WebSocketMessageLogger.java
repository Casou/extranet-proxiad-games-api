package com.proxiad.games.extranet.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

@Slf4j
public class WebSocketMessageLogger implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        if (!(message.getPayload() instanceof byte[]) || ((byte[]) message.getPayload()).length != 0) {
            log.debug(String.format("WEBSOCKET %s %s > %s",
                    message.getHeaders().get("simpDestination"),
                    message.getHeaders().get("simpMessageType"),
                    message.getPayload().toString()));
        }
        return message;
    }

}
