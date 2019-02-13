package com.proxiad.games.extranet.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum TimerStatusEnum {

	INITIALIZING, // En train de lire la vidéo
	STARTED,
	PAUSED,
	STOPPED

}
