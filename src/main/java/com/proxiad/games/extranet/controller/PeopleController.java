package com.proxiad.games.extranet.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.dto.PeopleNameDto;
import com.proxiad.games.extranet.model.People;
import com.proxiad.games.extranet.repository.PeopleRepository;

@RestController
public class PeopleController {

	@Autowired
	private PeopleRepository peopleRepository;

	@GetMapping("/people/all")
	public List<PeopleNameDto> getAll() {
		return StreamSupport.stream(peopleRepository.findAll().spliterator(), false)
				.map(PeopleNameDto::new)
				.collect(Collectors.toList());
	}

	@GetMapping("/people/{id}")
	public People getById(@PathVariable Integer id) {
		return peopleRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException(id + " not found"));
	}

}
