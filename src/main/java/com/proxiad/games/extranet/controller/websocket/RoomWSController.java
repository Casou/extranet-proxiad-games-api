package com.proxiad.games.extranet.controller.websocket;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.dto.RoomDto;
import com.proxiad.games.extranet.exception.ProxiadControllerException;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.repository.RoomRepository;

@RestController
public class RoomWSController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private RoomRepository roomRepository;

	@MessageMapping("/room/start")
	public void start(RoomDto roomDto) {
		this.simpMessagingTemplate.convertAndSend("/topic/room/all/start", roomDto);
		this.simpMessagingTemplate.convertAndSend("/topic/room/" + roomDto.getId() + "/start", roomDto);
	}

	@MessageMapping("/room/startTimer")
	public void startTimer(RoomDto roomDto) throws ProxiadControllerException {
		Optional<Room> optRoom = roomRepository.findById(roomDto.getId());
		if (!optRoom.isPresent()) {
			throw new ProxiadControllerException("Room " + roomDto.getId() + " doesn't exists");
		}

		Room room = optRoom.get();
		room.setStartTime(LocalDateTime.now());
		roomRepository.save(room);

		this.simpMessagingTemplate.convertAndSend("/topic/room/all/startTimer", room);
	}

}
