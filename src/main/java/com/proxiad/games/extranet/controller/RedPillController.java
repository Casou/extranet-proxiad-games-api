package com.proxiad.games.extranet.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.model.Riddle;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.repository.RiddleRepository;

@RestController
@CrossOrigin
public class RedPillController {

	@Autowired
	private RiddleRepository riddleRepository;

	@GetMapping("/redpill")
	public ResponseEntity<?> applyRedPill(@RequestAttribute Optional<Room> optRoom) {
		if (!optRoom.isPresent()) {
			return new ResponseEntity<>("Error in request (cannot retrieve room).", HttpStatus.BAD_REQUEST);
		}

		Room room = optRoom.get();
		List<Riddle> allRiddles = riddleRepository.findAll();

		if (allRiddles.stream().anyMatch(riddle -> !room.containsRiddle(riddle.getRiddleId()))) {
			return new ResponseEntity<>("You shouldn't have call the redpill command until all the riddles are resolved.", HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<>("Access granted", HttpStatus.OK);
	}

}
