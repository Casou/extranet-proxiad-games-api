package com.proxiad.games.extranet.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RoomDto {

	private Integer id;
	private String name;
	private String token;
	private List<Integer> resolvedRiddleIds = new ArrayList<>();

}
