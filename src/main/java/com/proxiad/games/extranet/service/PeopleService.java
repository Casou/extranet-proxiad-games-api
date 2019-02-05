package com.proxiad.games.extranet.service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proxiad.games.extranet.enums.SexeEnum;
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
		people.setName("Luc");
		people.setSurname("Diac");
		people.setEmail("luc.diac@proxiad.com");
		people.setSex(SexeEnum.MALE.getCode());
		people.setCity("Lille");
		people.setPictureIndex(124);
		people.setArrivalDate(LocalDateTime.now().minusYears(2).minusMonths(4).plusDays(16));
		people.setBirthDate(LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0));
		people.setPhone("06.42.11.42.23");
		peopleRepository.save(people);
		return people;
	}

	public int generatePreviousTeamEntries() {
		int nbPeopleSaved = 0;
		People people = new People();
		people.setName("Fleur");
		people.setSurname("Boncoeur");
		people.setEmail("f.boncoeur@proxiad.com");
		people.setSex(SexeEnum.FEMALE.getCode());
		people.setJob("Chef.fe de projet");
		people.setCity("Lille");
		people.setPictureIndex(80);
		people.setArrivalDate(LocalDateTime.of(1977, Month.APRIL, 3, 0, 0));
		people.setBirthDate(LocalDateTime.of(2009, Month.FEBRUARY, 1, 0, 0));
		people.setPhone("06.21.70.27.25");
		peopleRepository.save(people);
		nbPeopleSaved++;

		people = new People();
		people.setName("Lucie");
		people.setSurname("Cloutier");
		people.setEmail("l.cloutier@proxiad.com");
		people.setSex(SexeEnum.FEMALE.getCode());
		people.setJob("Développeur.euse Front");
		people.setCity("Lille");
		people.setPictureIndex(65);
		people.setArrivalDate(LocalDateTime.of(1981, Month.AUGUST, 30, 0, 0));
		people.setBirthDate(LocalDateTime.of(2011, Month.APRIL, 14, 0, 0));
		people.setPhone("06.91.05.64.83");
		peopleRepository.save(people);
		nbPeopleSaved++;

		people = new People();
		people.setName("Théo");
		people.setSurname("Dutrieux");
		people.setEmail("t.dutrieux@proxiad.com");
		people.setSex(SexeEnum.MALE.getCode());
		people.setJob("Développeur.euse Back");
		people.setCity("Lille");
		people.setPictureIndex(41);
		people.setArrivalDate(LocalDateTime.of(1992, Month.MAY, 28, 0, 0));
		people.setBirthDate(LocalDateTime.of(2014, Month.OCTOBER, 9, 0, 0));
		people.setPhone("06.69.77.25.53");
		peopleRepository.save(people);
		nbPeopleSaved++;

		people = new People();
		people.setName("Juliette");
		people.setSurname("Morneau");
		people.setEmail("j.morneau@proxiad.com");
		people.setSex(SexeEnum.FEMALE.getCode());
		people.setJob("Coach Agile");
		people.setCity("Lille");
		people.setPictureIndex(71);
		people.setArrivalDate(LocalDateTime.of(1984, Month.NOVEMBER, 21, 0, 0));
		people.setBirthDate(LocalDateTime.of(2014, Month.SEPTEMBER, 30, 0, 0));
		people.setPhone("06.64.78.56.21");
		peopleRepository.save(people);
		nbPeopleSaved++;

		people = new People();
		people.setName("Amaury");
		people.setSurname("Theriault");
		people.setEmail("a.theriault@proxiad.com");
		people.setSex(SexeEnum.MALE.getCode());
		people.setJob("Data analyst");
		people.setCity("Lille");
		people.setPictureIndex(53);
		people.setArrivalDate(LocalDateTime.of(1990, Month.NOVEMBER, 2, 0, 0));
		people.setBirthDate(LocalDateTime.of(2015, Month.SEPTEMBER, 15, 0, 0));
		people.setPhone("06.98.95.77.48");
		peopleRepository.save(people);
		nbPeopleSaved++;

		people = new People();
		people.setName("Elise");
		people.setSurname("Cliche");
		people.setEmail("e.cliche@proxiad.com");
		people.setSex(SexeEnum.FEMALE.getCode());
		people.setJob("Développeur.euse Back");
		people.setCity("Lille");
		people.setPictureIndex(30);
		people.setArrivalDate(LocalDateTime.of(1987, Month.JANUARY, 30, 0, 0));
		people.setBirthDate(LocalDateTime.of(2016, Month.MARCH, 1, 0, 0));
		people.setPhone("06.39.93.40.78");
		peopleRepository.save(people);
		nbPeopleSaved++;

		return nbPeopleSaved;
	}


}
