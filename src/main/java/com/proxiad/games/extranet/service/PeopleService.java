package com.proxiad.games.extranet.service;

import javax.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.proxiad.games.extranet.model.People;
import com.proxiad.games.extranet.repository.PeopleRepository;
import com.proxiad.games.extranet.utils.PeopleGenerator;

@Service
public class PeopleService {

	@Autowired
	private PeopleRepository peopleRepository;

	@Autowired
	private PeopleGenerator peopleGenerator;

	@Transactional
	public List<People> generatePeople(@PathVariable Integer numberOfPeople) {
		peopleRepository.deleteAll();
		List<People> people = peopleGenerator.generatePeople(numberOfPeople);
		peopleRepository.saveAll(people);
		return people;
	}
}
