package com.proxiad.games.extranet.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.dto.RoomDto;
import com.proxiad.games.extranet.enums.TimerStatusEnum;
import com.proxiad.games.extranet.mapper.RoomMapper;
import com.proxiad.games.extranet.model.Riddle;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.model.Timer;
import com.proxiad.games.extranet.repository.RiddleRepository;
import com.proxiad.games.extranet.repository.RoomRepository;

@RestController
@CrossOrigin
public class RedPillController {

	@Autowired
	private RiddleRepository riddleRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private RoomMapper roomMapper;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@GetMapping("/redpill")
	public ResponseEntity<?> applyRedPill(@RequestAttribute("room") Optional<Room> optRoom) {
		if (!optRoom.isPresent()) {
			return new ResponseEntity<>("Error in request (cannot retrieve room).", HttpStatus.BAD_REQUEST);
		}

		Room room = optRoom.get();
		Timer timer = room.getTimer();
		if (timer == null) {
			return new ResponseEntity<>("Error in request (timer not set).", HttpStatus.BAD_REQUEST);
		}

		List<Riddle> allRiddles = riddleRepository.findAll();

		if (allRiddles.stream().anyMatch(riddle -> !room.containsRiddle(riddle.getRiddleId()))) {
			return new ResponseEntity<>("You shouldn't have call the redpill command until all the riddles are resolved.", HttpStatus.FORBIDDEN);
		}

		timer.setStatus(TimerStatusEnum.PAUSED);
		timer.setRemainingTime(timer.calculatedRemainingTime());
		room.setTimer(timer);

		room.setIsTerminated(true);
		room.setTerminateStatus("success");
		roomRepository.save(room);

		RoomDto roomDto = roomMapper.toDto(room);
		this.simpMessagingTemplate.convertAndSend("/topic/room/" + room.getId() + "/terminate", roomDto);
		this.simpMessagingTemplate.convertAndSend("/topic/room/admin/success", roomDto);

		return new ResponseEntity<>("Access granted", HttpStatus.OK);
	}

}
