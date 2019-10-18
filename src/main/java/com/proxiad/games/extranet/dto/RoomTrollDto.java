package com.proxiad.games.extranet.dto;

import com.proxiad.games.extranet.model.Voice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomTrollDto {

	private Integer id;
	private String name;
	private String message;
	private Voice voice;
	private String videoName;
	private Integer reduceTime;
	private String startTime;
	private Integer remainingTime;

}
