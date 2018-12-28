package com.proxiad.games.extranet.controller.websocket;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.annotation.BypassSecurity;
import com.proxiad.games.extranet.dto.TokenDto;
import com.proxiad.games.extranet.service.AuthService;

@RestController
public class WSUserController {

	@Autowired
	private AuthService authService;

	private static final Map<String, String> CONNECTED_USERS_TOKEN = new HashMap<>();

	public void userConnected(String token, String sessionId) {
		Optional<TokenDto> optToken = authService.validateToken(token);
		optToken.ifPresent(tokenDto -> CONNECTED_USERS_TOKEN.put(sessionId, token));
	}

	public String userDisconnected(String sessionId) {
		String userToken = CONNECTED_USERS_TOKEN.get(sessionId);
		CONNECTED_USERS_TOKEN.remove(sessionId);
		return userToken;
	}

	@GetMapping("/connectedUsers")
	@BypassSecurity
	public List<String> listConnectedUsers() {
		return new ArrayList<>(CONNECTED_USERS_TOKEN.values());
	}

}
