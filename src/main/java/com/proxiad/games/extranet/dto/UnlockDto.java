package com.proxiad.games.extranet.dto;

import lombok.Data;

@Data
public class UnlockDto {

	private Integer id;
	private String password;

	private String riddleId;
	private Integer roomId;

	private String message;
	private String voice;
	private Integer nbRiddlesResolved;

}
