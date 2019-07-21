package com.proxiad.games.extranet.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

public class WebSocketMessageLogger implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        if (!(message.getPayload() instanceof byte[]) || ((byte[]) message.getPayload()).length != 0) {
            System.out.println(message.getHeaders().get("simpMessageType") + " > " + message.getPayload().toString());
        }
        return message;
    }

//    @Override
//    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
//        System.out.println(message);
//    }
//
//    @Override
//    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
//        System.out.println(message);
//    }
//
//    @Override
//    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
//        System.out.println(message);
//        return message;
//    }
}
