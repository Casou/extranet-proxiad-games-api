package com.proxiad.games.extranet.dto;

import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.model.Voice;

import lombok.Data;

@Data
public class RoomMessageDto {

	private Room room;
	private String message;
	private Voice voice;
	private TextDto introSentence;

}
