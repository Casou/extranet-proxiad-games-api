package com.proxiad.games.extranet.dto;

import com.proxiad.games.extranet.model.Voice;

import lombok.Data;

@Data
public class TextDto {

	private Integer id;
	private String text;
	private String voiceName;
	private Voice voice;

}
