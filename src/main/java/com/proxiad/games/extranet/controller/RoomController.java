package com.proxiad.games.extranet.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proxiad.games.extranet.annotation.AdminTokenSecurity;
import com.proxiad.games.extranet.dto.RiddleDto;
import com.proxiad.games.extranet.dto.RoomDto;
import com.proxiad.games.extranet.dto.RoomStatusDto;
import com.proxiad.games.extranet.mapper.RoomMapper;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.repository.RiddleRepository;
import com.proxiad.games.extranet.repository.RoomRepository;
import com.proxiad.games.extranet.service.RoomService;

@RestController
@CrossOrigin
public class RoomController {

	@Autowired
	private RoomService roomService;

	@Autowired
	private RiddleRepository riddleRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private RoomMapper roomMapper;

	@GetMapping("/unlock/status")
	public RoomStatusDto getRoomStatus(@RequestAttribute Optional<Room> room) {
		List<RiddleDto> riddleDtos = riddleRepository.findAll().stream()
				.map(RiddleDto::new)
				.peek(riddleDto -> riddleDto.setIsResolved(room.orElse(new Room()).containsRiddle(riddleDto.getRiddleId())))
				.sorted(Comparator.comparing(RiddleDto::getRiddleId))
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

	@PutMapping(value = "/room")
	@AdminTokenSecurity
	public ResponseEntity<?> newRoom() {
		Room room = new Room();
		room.setName("Nouveau");
		roomRepository.save(room);

		return new ResponseEntity<>(room, HttpStatus.OK);
	}

	@PostMapping(value = "/room/{id}/name")
	@AdminTokenSecurity
	public ResponseEntity<?> updateRoomName(@PathVariable("id") Integer id, @RequestBody RoomDto updatedRoom) {
		Optional<Room> optRoom = roomRepository.findById(id);

		if (!optRoom.isPresent()) {
			return new ResponseEntity<>("No room with id " + id, HttpStatus.BAD_REQUEST);
		}

		Room room = optRoom.get();
		room.setName(updatedRoom.getName());
		roomRepository.save(room);

		return new ResponseEntity<>(room, HttpStatus.OK);
	}

	@DeleteMapping(value = "/room/{id}")
	@AdminTokenSecurity
	public ResponseEntity<?> deleteRoom(@PathVariable("id") Integer id) {
		Optional<Room> optRoom = roomRepository.findById(id);

		if (!optRoom.isPresent()) {
			return new ResponseEntity<>("No room with id " + id, HttpStatus.BAD_REQUEST);
		}

		roomRepository.delete(optRoom.get());

		return new ResponseEntity<>("deleted", HttpStatus.OK);
	}

	@PatchMapping(value = "/room/{id}")
	@AdminTokenSecurity
	public ResponseEntity<?> reinitRoom(@PathVariable("id") Integer id) {
		Optional<Room> optRoom = roomRepository.findById(id);
		if (!optRoom.isPresent()) {
			return new ResponseEntity<>("No room with id " + id, HttpStatus.BAD_REQUEST);
		}

		Room room = optRoom.get();
		room.setResolvedRiddles(new ArrayList<>());
		room.setTimer(null);
		roomRepository.save(room);

		return new ResponseEntity<>(roomMapper.toDto(room), HttpStatus.OK);
	}

}
