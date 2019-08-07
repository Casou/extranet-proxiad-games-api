package com.proxiad.games.extranet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MandatoryParameter {

	INIT_TIMER_SECONDS("INIT_TIME", "3600", ParameterType.NUMBER, null),
	TERMINAL_TERMINATOR_COMMAND_RESPONSE("TERMINAL_TERMINATOR_COMMAND_RESPONSE", "...", ParameterType.FIELDSET, "terminal-style")
	;

	private String key;
	private String defaultValue;
	private ParameterType type;
	private String optionals;

}
