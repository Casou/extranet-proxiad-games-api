package com.proxiad.games.extranet.dto;

import com.proxiad.games.extranet.enums.RiddleType;

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
	private Integer roomId;
	private RiddleType type;

}
