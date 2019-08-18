package com.proxiad.games.extranet.dto;

import com.proxiad.games.extranet.model.Text;
import com.proxiad.games.extranet.model.Voice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TextDto {

	private Integer id;
	private String text;
	private String voiceName;
	private Voice voice;

	public TextDto(Text text) {
		this.id = text.getId();
		this.text = text.getText();
		this.voiceName = text.getVoiceName();
	}

}
