package com.proxiad.games.extranet.mapper;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.proxiad.games.extranet.dto.RoomDto;
import com.proxiad.games.extranet.model.Room;

@Component
public class RoomMapper {

	private static ModelMapper mapper = new ModelMapper();

	public RoomDto toDto(Room room) {
		RoomDto dto = mapper.map(room, RoomDto.class);
		dto.setToken(room.getConnectedToken() != null ? room.getConnectedToken().getToken() : null);
		Optional.ofNullable(room.getTimer()).ifPresent(timer -> {
			dto.setStartTime(timer.getStartTime());
			dto.setRemainingTime(Math.max(0, timer.getRemainingTime()));
			dto.setStatusTime(timer.getStatus());
		});
		return dto;
	}

}
