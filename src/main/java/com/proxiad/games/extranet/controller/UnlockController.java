package com.proxiad.games.extranet.controller;

import com.proxiad.games.extranet.annotation.AdminTokenSecurity;
import com.proxiad.games.extranet.dto.RiddleDto;
import com.proxiad.games.extranet.dto.RoomStatusDto;
import com.proxiad.games.extranet.dto.UnlockDto;
import com.proxiad.games.extranet.enums.RiddleType;
import com.proxiad.games.extranet.enums.TimerStatusEnum;
import com.proxiad.games.extranet.exception.PasswordDontMatchException;
import com.proxiad.games.extranet.exception.RiddleAlreadySolvedException;
import com.proxiad.games.extranet.model.*;
import com.proxiad.games.extranet.repository.VoiceRepository;
import com.proxiad.games.extranet.service.RiddleService;
import com.proxiad.games.extranet.service.TextService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class UnlockController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private TextService textService;

	@Autowired
	private VoiceRepository voiceRepository;

	@Autowired
	private RiddleService riddleService;

	private static final ModelMapper modelMapper = new ModelMapper();

	@GetMapping("/unlock/status")
	public RoomStatusDto getRoomStatus(@RequestAttribute("room") Optional<Room> optionalRoom) {
		Room room = optionalRoom.orElseThrow(() -> new EntityNotFoundException("Room not found with token association"));
		List<RiddleDto> riddleDtos = room.getRiddles().stream()
				.filter(riddle -> riddle.getType().equals(RiddleType.GAME))
				.map(riddle -> modelMapper.map(riddle, RiddleDto.class))
				.sorted(Comparator.comparing(RiddleDto::getRiddleId))
				.collect(Collectors.toList());

		return RoomStatusDto.builder()
				.riddles(riddleDtos)
				.build();
	}

	@PostMapping("/unlock")
	public ResponseEntity<?> unlockRiddle(@RequestBody UnlockDto unlockDto, @RequestAttribute("room") Optional<Room> optionalRoom) {
		Room room = optionalRoom.orElseThrow(() -> new EntityNotFoundException("Something is wrong with your token. Please clear the browser localStorage and login again."));

		Timer timer = room.getTimer();
		if (timer == null || !timer.getStatus().equals(TimerStatusEnum.STARTED)) {
			return new ResponseEntity<>("Timer is stopped.", HttpStatus.FORBIDDEN);
		}

		Riddle riddle;
		try {
			riddle = riddleService.resolveRiddle(unlockDto, room);
		} catch (PasswordDontMatchException | EntityNotFoundException ignored) {
			return new ResponseEntity<>("Id and password don't match.", HttpStatus.BAD_REQUEST);
		} catch (RiddleAlreadySolvedException ignored) {
			return new ResponseEntity<>("Riddle already unlocked.", HttpStatus.FORBIDDEN);
		}

		final Text textToSend = textService.getTextToSendForRiddleResolution(room);

		Voice voice = voiceRepository.findByName(textToSend.getVoiceName()).orElse(new Voice());

		unlockDto.setId(riddle.getId());
		unlockDto.setRoomId(room.getId());
		unlockDto.setNbRiddlesResolved((int) room.getRiddles().stream()
				.filter(r -> RiddleType.GAME.equals(r.getType()) && r.getResolved())
				.count());
		unlockDto.setMessage(textToSend.getText());
		unlockDto.setVoice(voice);
		this.simpMessagingTemplate.convertAndSend("/topic/riddle/unlock", unlockDto);
		this.simpMessagingTemplate.convertAndSend("/topic/room/" + room.getId() + "/unlockRiddle", unlockDto);

		return new ResponseEntity<>("unlocked", HttpStatus.OK);
	}

	@PostMapping(value = "/open-door")
	@AdminTokenSecurity
	public ResponseEntity<?> checkOpenDoor(@RequestBody RiddleDto riddleDto) {
		Riddle openDoorRiddle = riddleService.resolveOpenDoorRiddle(riddleDto);
		if (openDoorRiddle.getResolved()) {
			UnlockDto unlockDto = new UnlockDto();
			unlockDto.setId(openDoorRiddle.getId());
			unlockDto.setRoomId(riddleDto.getRoomId());
			this.simpMessagingTemplate.convertAndSend("/topic/riddle/unlock", unlockDto);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
	}

}
