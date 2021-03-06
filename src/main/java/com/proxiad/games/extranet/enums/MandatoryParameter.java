package com.proxiad.games.extranet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MandatoryParameter {

	INIT_TIMER_SECONDS("INIT_TIME", "3600", ParameterType.NUMBER, null),
	AUDIO_BACKGROUND_DEFAULT_VOLUME("AUDIO_BACKGROUND_DEFAULT_VOLUME", "10", ParameterType.NUMBER, null),
	PROGRESS_BAR_DURATION("PROGRESS_BAR_DURATION", "300", ParameterType.NUMBER, null),
	PROGRESS_BAR_SYNTHESIS("PROGRESS_BAR_SYNTHESIS", "true", ParameterType.BOOLEAN, null),
	TERMINAL_MINIMUM_LOCK_TIME("TERMINAL_MINIMUM_LOCK_TIME", "30", ParameterType.NUMBER, null),
	TERMINAL_MAXIMUM_LOCK_TIME("TERMINAL_MAXIMUM_LOCK_TIME", "60", ParameterType.NUMBER, null),
	TERMINAL_LOCK_TIME_STEP("TERMINAL_LOCK_TIME_STEP", "10", ParameterType.NUMBER, null),
	TROLL_DECREASE_TIME("TROLL_DECREASE_TIME", "120", ParameterType.NUMBER, null),
	TERMINAL_TERMINATOR_COMMAND_RESPONSE("TERMINAL_TERMINATOR_COMMAND_RESPONSE", "...", ParameterType.FIELDSET, "terminal-style")
	;

	private String key;
	private String defaultValue;
	private ParameterType type;
	private String optionals;

}
