package com.proxiad.games.extranet.service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public List<People> generateRandomPeople(Integer numberOfPeople) {
		peopleRepository.deleteAll();
		List<People> people = peopleGenerator.generatePeople(numberOfPeople);
		peopleRepository.saveAll(people);
		return people;
	}

	public People generateIAEntry() {
		People people = new People();
		people.setName("Glados");
		people.setSurname("AAAAAA");
		people.setEmail("glados@prodiad.fr");
		people.setSex(1);
		people.setCity("Lille");
		people.setPictureIndex(9999);
		people.setArrivalDate(LocalDateTime.now().minusYears(2).minusMonths(4).plusDays(16));
		people.setBirthDate(LocalDateTime.of(1982, Month.MAY, 15, 0, 0));
		people.setPhone("06.25.65.65.65");
		peopleRepository.save(people);
		return people;
	}

}
