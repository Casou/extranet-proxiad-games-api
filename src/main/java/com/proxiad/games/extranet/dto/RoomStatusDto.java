package com.proxiad.games.extranet.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomStatusDto {

	private List<RiddleDto> riddles;

}
