package com.proxiad.games.extranet.dto;

import com.proxiad.games.extranet.model.IntroSentence;
import com.proxiad.games.extranet.model.Room;

import lombok.Data;

@Data
public class RoomMessageDto {

	private Room room;
	private String message;
	private IntroSentence introSentence;

}
