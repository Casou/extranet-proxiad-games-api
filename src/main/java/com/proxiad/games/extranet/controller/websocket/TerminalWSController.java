package com.proxiad.games.extranet.controller.websocket;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.controller.ClientController;
import com.proxiad.games.extranet.dto.TerminalCommandDto;
import com.proxiad.games.extranet.dto.TokenDto;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.repository.RoomRepository;
import com.proxiad.games.extranet.service.AuthService;

@RestController
public class TerminalWSController {

	@Autowired
	private AuthService authService;

	@Autowired
	private ClientController wsUserController;

	@Autowired
	private RoomRepository roomRepository;

	@MessageMapping("/terminalCommand")
	@SendTo("/topic/riddle/terminalCommand")
	public TerminalCommandDto newTerminalCommand(@Header(value = "authorization") String authorizationToken, TerminalCommandDto terminalCommandDto) {
		Optional<TokenDto> validToken = authService.validateToken(authorizationToken);
		if (!validToken.isPresent()) {
			throw new RuntimeException("Invalid token : " + authorizationToken);
		}

		wsUserController.addCommandToUser(authorizationToken, terminalCommandDto);

		Optional<Room> optRoom = roomRepository.findByToken(authorizationToken);
		terminalCommandDto.setRoomId(optRoom.orElse(new Room()).getId());

		terminalCommandDto.setToken(authorizationToken);
		return terminalCommandDto;
	}

}
