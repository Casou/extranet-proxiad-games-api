package com.proxiad.games.extranet.controller;

import java.util.*;
import java.util.stream.Collectors;

import com.proxiad.games.extranet.dto.*;
import com.proxiad.games.extranet.model.Voice;
import com.proxiad.games.extranet.repository.VoiceRepository;
import com.proxiad.games.extranet.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.annotation.AdminTokenSecurity;
import com.proxiad.games.extranet.enums.TextEnum;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.model.Text;
import com.proxiad.games.extranet.repository.RoomRepository;
import com.proxiad.games.extranet.repository.TextRepository;
import com.proxiad.games.extranet.service.AuthService;

@RestController
public class ClientController {

	@Autowired
	private AuthService authService;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private TextRepository textRepository;

	@Autowired
	private TextService textService;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	private static final Map<String, UserSessionDto> CONNECTED_USERS_TOKEN = new HashMap<>();
	private static final Map<String, RoomSessionDto> CONNECTED_ROOMS = new HashMap<>();

	@GetMapping("/connectedUsers")
	@AdminTokenSecurity
	public List<UserSessionDto> listConnectedUsers() {
		return CONNECTED_USERS_TOKEN.values().stream()
				.filter(UserSessionDto::getIsConnected)
				.collect(Collectors.toList());
	}

	@GetMapping("/connectedRooms")
	@AdminTokenSecurity
	public List<RoomSessionDto> listConnectedRooms() {
		return CONNECTED_ROOMS.values().stream()
				.filter(RoomSessionDto::getIsConnected)
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
		if (CONNECTED_USERS_TOKEN.containsKey(token)) {
			Text text = textRepository.findAllByDiscriminantOrderByIdAsc(TextEnum.OPEN_TERMINAL).get(0);
			TextDto textDto = textService.mapToDto(text);

			UserSessionDto userSessionDto = CONNECTED_USERS_TOKEN.get(token);
			userSessionDto.setSessionId(sessionId);
			userSessionDto.setIsConnected(true);
			userSessionDto.setMessage(textDto);
			this.simpMessagingTemplate.convertAndSend("/topic/user/connected", userSessionDto);
			this.simpMessagingTemplate.convertAndSend("/topic/user/" + userSessionDto.getRoomId() + "/connected", userSessionDto);
			return;
		}

		Optional<Room> optRoom = roomRepository.findByToken(token);
		Optional<TokenDto> optToken = authService.validateToken(token);

		optToken.ifPresent(tokenDto -> {
			Text text = textRepository.findAllByDiscriminantOrderByIdAsc(TextEnum.OPEN_TERMINAL).get(0);
			TextDto textDto = textService.mapToDto(text);

			UserSessionDto userSessionDto = UserSessionDto.builder()
					.token(token)
					.sessionId(sessionId)
					.roomId(optRoom.orElse(new Room()).getId())
					.isConnected(true)
					.message(textDto)
					.build();

			CONNECTED_USERS_TOKEN.put(token, userSessionDto);
			this.simpMessagingTemplate.convertAndSend("/topic/user/connected", userSessionDto);
			this.simpMessagingTemplate.convertAndSend("/topic/user/" + userSessionDto.getRoomId() + "/connected", userSessionDto);
		});
	}

	public void roomConnected(Integer roomId, String sessionId) {
		Optional<Room> optRoom = roomRepository.findById(roomId);

		optRoom.ifPresent(roomEntity -> {
			RoomSessionDto roomSessionDto = RoomSessionDto.builder()
					.id(roomEntity.getId())
					.name(roomEntity.getName())
					.sessionId(sessionId)
					.isConnected(true)
					.build();

			CONNECTED_ROOMS.put(roomEntity.getName(), roomSessionDto);
			this.simpMessagingTemplate.convertAndSend("/topic/room/connected", roomSessionDto);
		});
	}

	public Optional<String> userDisconnected(String sessionId) {
		Optional<Map.Entry<String, UserSessionDto>> mapEntry = CONNECTED_USERS_TOKEN.entrySet().stream()
				.filter(entry -> entry.getValue().getSessionId().equals(sessionId))
				.findFirst();
		if (!mapEntry.isPresent()) {
			return Optional.empty();
		}

		UserSessionDto userSessionDto = mapEntry.get().getValue();
		userSessionDto.setIsConnected(false);

		this.simpMessagingTemplate.convertAndSend("/topic/user/disconnected", userSessionDto);
		return Optional.of(userSessionDto.getToken());
	}

	public Optional<String> roomDisconnected(String sessionId) {
		Optional<Map.Entry<String, RoomSessionDto>> mapEntry = CONNECTED_ROOMS.entrySet().stream()
				.filter(entry -> entry.getValue().getSessionId().equals(sessionId))
				.findFirst();
		if (!mapEntry.isPresent()) {
			return Optional.empty();
		}

		RoomSessionDto roomSessionDto = mapEntry.get().getValue();
		roomSessionDto.setIsConnected(false);
		this.simpMessagingTemplate.convertAndSend("/topic/room/disconnected", roomSessionDto);
		return Optional.of(roomSessionDto.getName());
	}

}
