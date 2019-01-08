package com.proxiad.games.extranet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proxiad.games.extranet.dto.RoomDto;
import com.proxiad.games.extranet.mapper.RoomMapper;
import com.proxiad.games.extranet.repository.RoomRepository;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private RoomMapper roomMapper;

	public List<RoomDto> findAll() {
		return roomRepository.findAll().stream()
				.map(room -> roomMapper.toDto(room))
				.collect(Collectors.toList());
	}

}
