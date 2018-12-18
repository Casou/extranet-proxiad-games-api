package com.proxiad.games.extranet.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.dto.PeopleDto;
import com.proxiad.games.extranet.dto.PeopleNameDto;
import com.proxiad.games.extranet.model.People;
import com.proxiad.games.extranet.repository.PeopleRepository;
import com.proxiad.games.extranet.utils.DateUtils;

@RestController
public class PeopleController {

	@Autowired
	private PeopleRepository peopleRepository;

	private static ModelMapper mapper = new ModelMapper();

	@GetMapping("/people/all")
	public List<PeopleNameDto> getAll() {
		return StreamSupport.stream(
				peopleRepository.findAllByOrderBySurnameAscNameAsc().spliterator(), false)
				.map(PeopleNameDto::new)
				.sorted((p1, p2) -> {
					if (p1.getSurname().compareTo(p2.getSurname()) == 0) {
						return p1.getSurname().compareTo(p2.getName());
					}
					return p1.getSurname().compareTo(p2.getSurname());
				})
				.collect(Collectors.toList());
	}

	@GetMapping("/people/{id}")
	public PeopleDto getById(@PathVariable Integer id) {
		People people = peopleRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException(id + " not found"));

		PeopleDto dto = mapper.map(people, PeopleDto.class);
		dto.setAge(DateUtils.calculateAge(people.getBirthDate().toLocalDate(), LocalDateTime.now().toLocalDate()));
		dto.setSkillMap(people.getSkills().entrySet().stream()
			.collect(Collectors.toMap(entry -> entry.getKey(), entry -> Arrays.asList(entry.getValue().split(",")))));
		return dto;
	}

}
