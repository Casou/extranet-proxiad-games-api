package com.proxiad.games.extranet.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomTrollDto {

	private Integer id;
	private String name;
	private Integer reduceTime;
	private LocalDateTime startTime;
	private Integer remainingTime;

}
