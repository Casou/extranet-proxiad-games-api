package com.proxiad.games.extranet.dto;

import com.proxiad.games.extranet.enums.ParameterEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParameterDto {

	private Integer id;
	private String key;
	private String description;
	private String value;

	public ParameterDto(ParameterEnum parameterEnum) {
		this.key = parameterEnum.getKey();
		this.value = parameterEnum.getDefaultValue();
	}

}
