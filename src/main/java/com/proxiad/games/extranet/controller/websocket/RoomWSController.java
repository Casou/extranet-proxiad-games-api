package com.proxiad.games.extranet.controller.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.dto.RoomDto;

@RestController
public class RoomWSController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/room/start")
	public void start(RoomDto roomDto) {
		this.simpMessagingTemplate.convertAndSend("/topic/room/all/start", roomDto);
		this.simpMessagingTemplate.convertAndSend("/topic/room/" + roomDto.getId() + "/start", roomDto);
	}

	@MessageMapping("/room/startTimer")
	public void startTimer(RoomDto roomDto) {
		this.simpMessagingTemplate.convertAndSend("/topic/room/all/startTimer", roomDto);
	}

}
