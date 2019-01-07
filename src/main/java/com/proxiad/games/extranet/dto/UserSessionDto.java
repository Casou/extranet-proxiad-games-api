package com.proxiad.games.extranet.dto;

import java.util.LinkedList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSessionDto {

	private String token;
	private Integer roomId;
	private String sessionId;
	private Boolean isConnected;
	@Builder.Default
	private List<TerminalCommandDto> commands = new LinkedList<>();

}
