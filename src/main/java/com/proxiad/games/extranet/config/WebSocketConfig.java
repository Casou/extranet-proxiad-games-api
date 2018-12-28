package com.proxiad.games.extranet.config;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.proxiad.games.extranet.controller.websocket.WSUserController;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Autowired
	private WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;

	@Autowired
	private WSUserController wsUserController;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .withSockJS()
				.setInterceptors(webSocketHandshakeInterceptor)
				.setClientLibraryUrl("/resources/sockjs-1.1.5.js")
		;
    }

	@EventListener(SessionConnectEvent.class)
	public void handleWebsocketConnectListner(SessionConnectEvent event) {
		GenericMessage message = (GenericMessage) event.getMessage();

		MessageHeaders headers = message.getHeaders();
		if (headers == null) {
			log.info("Received a new web socket connection (no header) : " + event);
		}

		Optional<Map> nativeHeaders = Optional.of((Map) headers.get("nativeHeaders"));
		if (!nativeHeaders.isPresent()) {
			log.info("Received a new web socket connection (no native header) : " + event);
		}

		Optional<List> authorizationHeaderList = Optional.of((List) nativeHeaders.get().get("Authorization"));
		if (!authorizationHeaderList.isPresent()) {
			log.info("Received a new web socket connection (no authorization in native header) : " + event);
		}

		String token = (String) authorizationHeaderList.get().get(0);
		String sessionId = (String) headers.get("simpSessionId");
		if (token != null && sessionId != null) {
			wsUserController.userConnected(token, sessionId);
		}
		log.info("Received a new web socket connection from [" + token + ", " + sessionId + "] : " + event);
	}

	@EventListener(SessionDisconnectEvent.class)
	public void handleWebsocketDisconnectListner(SessionDisconnectEvent event) {
		String token = wsUserController.userDisconnected(event.getSessionId());

		log.info("User disconnected [" + token + ", " + event.getSessionId() + "] : " + event);
	}

}