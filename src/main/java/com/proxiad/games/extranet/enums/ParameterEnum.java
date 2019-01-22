package com.proxiad.games.extranet.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParameterEnum {

	INIT_TIMER_SECONDS("INIT_TIME", "3600");

	private String key;
	private String defaultValue;

	public static Optional<ParameterEnum> findByKey(String key) {
		return Optional.ofNullable(Arrays.stream(ParameterEnum.values())
				.filter(parameterEnum -> parameterEnum.getKey().equals(key))
				.findFirst()
				.orElse(null));
	}

}
