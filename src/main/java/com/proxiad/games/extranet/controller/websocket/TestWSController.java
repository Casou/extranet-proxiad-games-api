package com.proxiad.games.extranet.controller.websocket;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.proxiad.games.extranet.dto.TokenDto;
import com.proxiad.games.extranet.service.AuthService;

@Controller
public class TestWSController {

	@Autowired
	private AuthService authService;

	@MessageMapping("/test")
	@SendTo("/topic/test")
	public String message(@Header(value = "authorization") String authorizationToken) {
		Optional<TokenDto> validToken = authService.validateToken(authorizationToken);
		if (!validToken.isPresent()) {
			throw new RuntimeException("Invalid token : " + authorizationToken);
		}

		return "aaaaaah !!!!";
	}
}
