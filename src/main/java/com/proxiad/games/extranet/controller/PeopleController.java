package com.proxiad.games.extranet.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.dto.PeopleDto;

@RestController
public class PeopleController {

	@GetMapping("/people/all")
	public List<PeopleDto> getAll() {
		return new ArrayList<>();
	}

}
