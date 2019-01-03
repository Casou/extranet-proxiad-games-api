package com.proxiad.games.extranet.config;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.proxiad.games.extranet.controller.websocket.WSClientController;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Autowired
	private WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;

	@Autowired
	private WSClientController wsUserController;

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
		String sessionId = (String) headers.get("simpSessionId");
		if (headers == null) {
			log.info("Received a new web socket connection (no header) : " + event);
		}

		Optional<Map> nativeHeaders = Optional.of((Map) headers.get("nativeHeaders"));
		if (!nativeHeaders.isPresent()) {
			log.info("Received a new web socket connection (no native header) : " + event);
		}

		checkToken(event, sessionId, nativeHeaders.get());
		checkRoomToken(event, sessionId, nativeHeaders.get());

		log.info("Received a new web socket connection from [" + sessionId + "] : " + event);
	}

	private void checkToken(SessionConnectEvent event, String sessionId, Map nativeHeaders) {
		Optional<List> authorizationHeaderList = Optional.ofNullable((List) nativeHeaders.get("Authorization"));
		if (!authorizationHeaderList.isPresent()) {
			log.info("Received a new web socket connection (no authorization in native header) : " + event);
			return;
		}

		String token = (String) authorizationHeaderList.get().get(0);
		if (token != null && sessionId != null) {
			wsUserController.userConnected(token, sessionId);
		}
	}

	private void checkRoomToken(SessionConnectEvent event, String sessionId, Map nativeHeaders) {
		Optional<List> roomHeaderList = Optional.ofNullable((List) nativeHeaders.get("Room"));
		if (!roomHeaderList.isPresent()) {
			log.info("Received a new web socket connection (no authorization in native header) : " + event);
			return;
		}

		String roomId = (String) roomHeaderList.get().get(0);
		if (!StringUtils.isEmpty(roomId) && sessionId != null) {
			wsUserController.roomConnected(Integer.valueOf(roomId), sessionId);
		}
	}

	@EventListener(SessionDisconnectEvent.class)
	public void handleWebsocketDisconnectListner(SessionDisconnectEvent event) {
		Optional<String> optToken = wsUserController.userDisconnected(event.getSessionId());
		optToken.ifPresent(token -> log.info("User disconnected [" + optToken + ", " + event.getSessionId() + "] : " + event));

		Optional<String> optRoom = wsUserController.roomDisconnected(event.getSessionId());
		optRoom.ifPresent(room -> log.info("Room disconnected [" + room + ", " + event.getSessionId() + "] : " + event));
	}

}