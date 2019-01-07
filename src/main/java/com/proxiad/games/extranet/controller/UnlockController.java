package com.proxiad.games.extranet.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import com.proxiad.games.extranet.dto.UnlockDto;
import com.proxiad.games.extranet.model.Riddle;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.repository.RiddleRepository;
import com.proxiad.games.extranet.repository.RoomRepository;

@RestController
@CrossOrigin
public class UnlockController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private RiddleRepository riddleRepository;

	@PostMapping("/unlock")
	public ResponseEntity<?> unlockRiddle(@RequestBody UnlockDto unlockDto, @RequestAttribute String token) {
		final List<Riddle> allRiddles = riddleRepository.findAll();
		final Optional<Riddle> optResolvedRiddle = allRiddles.stream().filter(riddle -> riddle.getRiddleId().equals(unlockDto.getRiddleId())
				&& riddle.getRiddlePassword().equals(unlockDto.getPassword())).findFirst();
		if (!optResolvedRiddle.isPresent()) {
			return new ResponseEntity<>("Id and password don't match.", HttpStatus.FORBIDDEN);
		}

		Optional<Room> optRoom = roomRepository.findByToken(token);
		if (!optRoom.isPresent()) {
			return new ResponseEntity<>("Something is wrong with your token. Please clear the browser localStorage and login again.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Room room = optRoom.get();

		List<Riddle> resolvedRiddles = room.getResolvedRiddles();
		if (resolvedRiddles.stream().anyMatch(riddle -> riddle.getRiddleId().equals(unlockDto.getId()))) {
			return new ResponseEntity<>("Riddle already unlocked.", HttpStatus.FORBIDDEN);
		}

		Riddle riddle = optResolvedRiddle.get();
		room.getResolvedRiddles().add(riddle);
		roomRepository.save(room);

		unlockDto.setId(riddle.getId());
		unlockDto.setRoomId(room.getId());
		this.simpMessagingTemplate.convertAndSend("/topic/riddle/unlock", unlockDto);

		return new ResponseEntity<>("unlocked", HttpStatus.OK);
	}

}
