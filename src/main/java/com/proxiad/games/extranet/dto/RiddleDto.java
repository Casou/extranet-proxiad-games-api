package com.proxiad.games.extranet.dto;

import com.proxiad.games.extranet.model.Riddle;

import lombok.Data;

@Data
public class RiddleDto {

	private String riddleId;
	private Boolean isResolved;

	public RiddleDto(Riddle riddle) {
		this.riddleId = riddle.getRiddleId();
		this.isResolved = false;
	}

}
