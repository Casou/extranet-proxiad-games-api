package com.proxiad.games.extranet.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomSessionDto {

	private Integer id;
	private String name;
	private String sessionId;
	private Boolean isConnected;

}
