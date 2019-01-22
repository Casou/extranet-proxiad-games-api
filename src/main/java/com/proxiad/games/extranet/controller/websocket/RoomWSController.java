package com.proxiad.games.extranet.controller.websocket;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.dto.RoomDto;
import com.proxiad.games.extranet.dto.RoomMessageDto;
import com.proxiad.games.extranet.enums.TimerStatusEnum;
import com.proxiad.games.extranet.exception.ProxiadControllerException;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.model.Timer;
import com.proxiad.games.extranet.repository.RoomRepository;

@RestController
public class RoomWSController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private RoomRepository roomRepository;

	@MessageMapping("/room/message")
	public void sendMessage(RoomMessageDto messageDto) {
		this.simpMessagingTemplate.convertAndSend("/topic/room/" + messageDto.getRoom().getId() + "/message", messageDto);
	}

	@MessageMapping("/room/fail")
	@SendTo("/topic/room/all/fail")
	public RoomDto roomFailed(RoomDto roomDto) throws ProxiadControllerException {
		Optional<Room> optRoom = roomRepository.findById(roomDto.getId());
		if (!optRoom.isPresent()) {
			throw new ProxiadControllerException("Error in request (cannot retrieve room).");
		}

		Room room = optRoom.get();
		Timer timer = room.getTimer();
		if (timer == null) {
			throw new ProxiadControllerException("Error in request (timer not set).");
		}

		timer.setStatus(TimerStatusEnum.PAUSED);
		timer.setRemainingTime(0);
		room.setTimer(timer);

		room.setIsTerminated(true);
		room.setTerminateStatus("fail");
		roomRepository.save(room);

		return roomDto;
	}

	@MessageMapping("/room/refresh")
	public void refreshFront(RoomDto roomDto) {
		this.simpMessagingTemplate.convertAndSend("/topic/refresh/" + roomDto.getId(), roomDto);
	}

}
