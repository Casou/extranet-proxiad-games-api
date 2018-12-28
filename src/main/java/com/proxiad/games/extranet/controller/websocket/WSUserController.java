package com.proxiad.games.extranet.controller.websocket;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.annotation.AdminTokenSecurity;
import com.proxiad.games.extranet.dto.TerminalCommandDto;
import com.proxiad.games.extranet.dto.TokenDto;
import com.proxiad.games.extranet.dto.UserSessionDto;
import com.proxiad.games.extranet.service.AuthService;

@RestController
public class WSUserController {

	@Autowired
	private AuthService authService;

	private static final Map<String, UserSessionDto> CONNECTED_USERS_TOKEN = new HashMap<>();

	@GetMapping("/connectedUsers")
	@AdminTokenSecurity
	public List<UserSessionDto> listConnectedUsers() {
		return CONNECTED_USERS_TOKEN.values().stream()
				.filter(UserSessionDto::getIsConnected)
				.collect(Collectors.toList());
	}

	@GetMapping("/users")
	@AdminTokenSecurity
	public List<UserSessionDto> listAllUsers() {
		return new ArrayList<>(CONNECTED_USERS_TOKEN.values());
	}


	public UserSessionDto getUser(String token) {
		return CONNECTED_USERS_TOKEN.get(token);
	}

	public void addCommandToUser(String token, TerminalCommandDto terminalCommandDto) {
		UserSessionDto userSessionDto = this.getUser(token);
		userSessionDto.getCommands().add(terminalCommandDto);
		CONNECTED_USERS_TOKEN.put(token, userSessionDto);
	}

	public void userConnected(String token, String sessionId) {
		Optional<TokenDto> optToken = authService.validateToken(token);
		optToken.ifPresent(tokenDto ->
				CONNECTED_USERS_TOKEN.put(token, UserSessionDto.builder()
						.token(token)
						.sessionId(sessionId)
						.isConnected(true)
						.build()));
	}

	public String userDisconnected(String sessionId) {
		UserSessionDto userSessionDto = CONNECTED_USERS_TOKEN.entrySet().stream()
				.filter(entry -> entry.getValue().getSessionId().equals(sessionId))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No user found for sessionId : " + sessionId))
				.getValue();
		userSessionDto.setIsConnected(false);
		return userSessionDto.getToken();
	}

}
