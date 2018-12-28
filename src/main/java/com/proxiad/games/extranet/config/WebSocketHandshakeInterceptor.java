package com.proxiad.games.extranet.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.proxiad.games.extranet.service.AuthService;

@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

	@Autowired
	private AuthService authService;

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
								   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
		// Set ip attribute to WebSocket session
		attributes.put("ip", request.getRemoteAddress());

//		TODO Faire un check de sécurité
//		ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
//		HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
//
//		String token = httpServletRequest.getParameter("token");
//		Optional<Token> validToken = authService.validateToken(token);
//
//		return validToken.isPresent();

		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

	}

}
