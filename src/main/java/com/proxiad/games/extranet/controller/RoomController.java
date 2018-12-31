package com.proxiad.games.extranet.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.annotation.AdminTokenSecurity;
import com.proxiad.games.extranet.dto.RiddleDto;
import com.proxiad.games.extranet.dto.RoomDto;
import com.proxiad.games.extranet.dto.RoomStatusDto;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.repository.RiddleRepository;
import com.proxiad.games.extranet.service.RoomService;

@RestController
public class RoomController {

	@Autowired
	private RoomService roomService;

	@Autowired
	private RiddleRepository riddleRepository;

	@GetMapping("/unlock/status")
	public RoomStatusDto getRoomStatus(@RequestAttribute Optional<Room> room) {
		List<RiddleDto> riddleDtos = riddleRepository.findAll().stream()
				.map(RiddleDto::new)
				.peek(riddleDto -> riddleDto.setIsResolved(room.orElse(new Room()).getResolvedRiddleIds().contains(riddleDto.getRiddleId())))
				.collect(Collectors.toList());

		return RoomStatusDto.builder()
				.riddles(riddleDtos)
				.build();
	}

	@GetMapping("/rooms")
	@AdminTokenSecurity
	public List<RoomDto> listAllRooms() {
		return roomService.findAll();
	}

}
