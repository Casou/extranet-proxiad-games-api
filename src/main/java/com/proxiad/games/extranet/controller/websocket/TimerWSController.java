package com.proxiad.games.extranet.controller.websocket;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.dto.RoomDto;
import com.proxiad.games.extranet.dto.RoomTrollDto;
import com.proxiad.games.extranet.enums.TimerStatusEnum;
import com.proxiad.games.extranet.exception.ProxiadControllerException;
import com.proxiad.games.extranet.mapper.RoomMapper;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.model.Timer;
import com.proxiad.games.extranet.repository.RoomRepository;

@RestController
public class TimerWSController {

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
		timer.setStatus(TimerStatusEnum.INITIALIZING);
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

	@MessageMapping("/room/reduceTime")
	@SendTo("/topic/room/all/reduceTime")
	public RoomTrollDto reduceTimerByTroll(RoomTrollDto roomTrollDto) throws ProxiadControllerException {
		Optional<Room> optRoom = roomRepository.findById(roomTrollDto.getId());
		if (!optRoom.isPresent()) {
			throw new ProxiadControllerException("Room with id " + roomTrollDto.getId() + " not found");
		}

		Timer timer = optRoom.get().getTimer();
		roomTrollDto.setStartTime(timer.getStartTime());
		roomTrollDto.setRemainingTime(timer.getRemainingTime());

		return roomTrollDto;
	}

	@MessageMapping("/room/pause")
	public void pause(RoomDto roomDto) throws ProxiadControllerException {
		final Room room = getRoom(roomDto);

		final Timer timer = Optional.ofNullable(room.getTimer()).orElseThrow(() -> new ProxiadControllerException("No timer found for the room " + room.getName()));

		timer.setStartTime(LocalDateTime.now());
		timer.setStatus(TimerStatusEnum.PAUSED);
		timer.setRemainingTime(timer.calculatedRemainingTime());

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

}
