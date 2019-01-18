package com.proxiad.games.extranet.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomTrollDto {

	private Integer id;
	private String name;
	private Integer reduceTime;

}
