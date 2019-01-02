package com.proxiad.games.extranet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proxiad.games.extranet.dto.RoomDto;
import com.proxiad.games.extranet.repository.RoomRepository;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;

	private static ModelMapper mapper = new ModelMapper();

	public List<RoomDto> findAll() {
		return roomRepository.findAll().stream()
				.map(room -> {
					RoomDto dto = mapper.map(room, RoomDto.class);
					dto.setToken(room.getConnectedToken() != null ? room.getConnectedToken().getToken() : null);
					return dto;
				})
				.collect(Collectors.toList());
	}

}