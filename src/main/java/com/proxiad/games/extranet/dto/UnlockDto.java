package com.proxiad.games.extranet.dto;

import com.proxiad.games.extranet.model.Voice;
import lombok.Data;

@Data
public class UnlockDto {

	private Integer id;
	private String password;

	private String riddleId;
	private Integer roomId;

	private String message;
	private Voice voice;
	private Integer nbRiddlesResolved;

}
