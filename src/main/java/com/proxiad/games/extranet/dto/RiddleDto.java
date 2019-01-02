package com.proxiad.games.extranet.dto;

import com.proxiad.games.extranet.model.Riddle;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RiddleDto {

	private Integer id;
	private String name;
	private String riddleId;
	private String riddlePassword;
	private Boolean isResolved;

	public RiddleDto(Riddle riddle) {
		this.id = riddle.getId();
		this.name = riddle.getName();
		this.riddleId = riddle.getRiddleId();
		this.riddlePassword = riddle.getRiddlePassword();
		this.isResolved = false;
	}

}
