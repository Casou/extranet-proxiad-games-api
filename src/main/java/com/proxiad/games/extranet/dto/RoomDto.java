package com.proxiad.games.extranet.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.proxiad.games.extranet.enums.TimerStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

	private Integer id;
	private String name;
	private String token;
	private List<RiddleDto> resolvedRiddles = new ArrayList<>();
	private Boolean isTerminated;

	private TimerStatusEnum statusTime;
	private LocalDateTime startTime;
	private Integer remainingTime;

}
