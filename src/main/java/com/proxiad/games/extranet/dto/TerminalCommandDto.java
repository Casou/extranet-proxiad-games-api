package com.proxiad.games.extranet.dto;

import lombok.Data;

@Data
public class TerminalCommandDto {

	private Integer roomId;
	private String token;
	private String command;
	private String text;
	private String status;
	private Boolean isProgress;

}
