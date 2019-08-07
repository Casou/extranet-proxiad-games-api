package com.proxiad.games.extranet.dto;

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
	private String voice;
	private String videoName;
	private Integer reduceTime;
	private String startTime;
	private Integer remainingTime;

}
