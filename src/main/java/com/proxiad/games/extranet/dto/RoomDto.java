package com.proxiad.games.extranet.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
	private List<Integer> resolvedRiddleIds = new ArrayList<>();
	private LocalDateTime startTime;

}
