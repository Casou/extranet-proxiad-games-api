package com.proxiad.games.extranet.controller.websocket;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.dto.RoomDto;
import com.proxiad.games.extranet.dto.RoomMessageDto;
import com.proxiad.games.extranet.enums.TimerStatusEnum;
import com.proxiad.games.extranet.exception.ProxiadControllerException;
import com.proxiad.games.extranet.mapper.RoomMapper;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.model.Timer;
import com.proxiad.games.extranet.repository.RoomRepository;

@RestController
public class RoomWSController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private RoomMapper roomMapper;


	@MessageMapping("/room/start")
	public void start(RoomDto roomDto) throws ProxiadControllerException {
		final Room room = getRoom(roomDto);

		final Timer timer = Optional.ofNullable(room.getTimer()).orElse(new Timer());
		timer.setRemainingTime(Math.max(0, roomDto.getRemainingTime()));
		room.setTimer(timer);
		roomRepository.save(room);

		this.simpMessagingTemplate.convertAndSend("/topic/room/all/start", roomDto);
		this.simpMessagingTemplate.convertAndSend("/topic/room/" + roomDto.getId() + "/start", roomDto);
	}

	@MessageMapping("/room/startTimer")
	public void startTimer(RoomDto roomDto) throws ProxiadControllerException {
		final Room room = getRoom(roomDto);

		final Timer timer = Optional.ofNullable(room.getTimer()).orElseThrow(() -> new ProxiadControllerException("No timer found for the room " + room.getName()));
		timer.setStartTime(LocalDateTime.now());
		timer.setStatus(TimerStatusEnum.STARTED);
		room.setTimer(timer);
		roomRepository.save(room);

		this.simpMessagingTemplate.convertAndSend("/topic/room/all/startTimer", roomMapper.toDto(room));
	}


	@MessageMapping("/room/pause")
	public void pause(RoomDto roomDto) throws ProxiadControllerException {
		final Room room = getRoom(roomDto);

		final Timer timer = Optional.ofNullable(room.getTimer()).orElseThrow(() -> new ProxiadControllerException("No timer found for the room " + room.getName()));
		final long remainingTime = timer.getRemainingTime() - timer.getStartTime().until(LocalDateTime.now(), ChronoUnit.SECONDS);

		timer.setStartTime(LocalDateTime.now());
		timer.setStatus(TimerStatusEnum.PAUSED);
		timer.setRemainingTime(Math.max(0, Math.toIntExact(remainingTime)));

		room.setTimer(timer);
		roomRepository.save(room);

		RoomDto payload = roomMapper.toDto(room);
		this.simpMessagingTemplate.convertAndSend("/topic/room/all/pause", payload);
		this.simpMessagingTemplate.convertAndSend("/topic/room/" + roomDto.getId() + "/pause", payload);
	}

	private Room getRoom(RoomDto roomDto) throws ProxiadControllerException {
		return roomRepository.findById(roomDto.getId())
				.orElseThrow(() -> new ProxiadControllerException("Room " + roomDto.getId() + " doesn't exists"));
	}

	@MessageMapping("/room/message")
	public void sendMessage(RoomMessageDto messageDto) throws ProxiadControllerException {
		this.simpMessagingTemplate.convertAndSend("/topic/room/" + messageDto.getRoom().getId() + "/message", messageDto);
	}

}
