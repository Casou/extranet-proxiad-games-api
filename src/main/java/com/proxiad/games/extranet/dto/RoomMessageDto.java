package com.proxiad.games.extranet.dto;

import com.proxiad.games.extranet.model.Text;
import com.proxiad.games.extranet.model.Room;

import lombok.Data;

@Data
public class RoomMessageDto {

	private Room room;
	private String message;
	private String voice;
	private Text introSentence;

}
