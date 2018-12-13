package com.proxiad.games.extranet.utils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.proxiad.games.extranet.model.People;

import lombok.Getter;

public class GeneratePeople {

	private static final Map<Integer, Integer> MAP_MAX_PICTURE_INDEX_BY_SEX = new HashMap<Integer, Integer>() {{
		put(1, 26);
		put(2, 26);
	}};
	private static final List<String> CITIES = Arrays.asList("Lille",
			"Paris",
			"Bordeaux",
			"Nantes",
			"Rouen",
			"Aix en Provence",
			"Bulgarie",
			"Strasbourg",
			"Lyon");

	private LocalDateTime birthdayMinDate;
	private LocalDateTime arrivalMaxDate;

	GeneratePeople() {
		this.birthdayMinDate = LocalDateTime.of(1975, Month.JANUARY, 1, 0, 0);
		this.arrivalMaxDate = LocalDateTime.now().minusMonths(3);
	}

	public static void main(String[] args) {
		GeneratePeople generator = new GeneratePeople();
		generator.generatePeople(200);
	}


	public List<People> generatePeople(Integer nbPeople) {
		File firstNameReferential = new File(GeneratePeopleReferential.FIRSTNAME_REFERENTIAL);
		File surnameReferential = new File(GeneratePeopleReferential.SURNAME_REFERENTIAL);

		List<FirstNameRef> firstNameRef = FileUtils.readFile(firstNameReferential).stream().map(FirstNameRef::new).collect(Collectors.toList());
		List<SurnameRef> surnameRef = FileUtils.readFile(surnameReferential).stream().map(SurnameRef::new).collect(Collectors.toList());

		List<FirstNameRef> flatFirstNameRef = (List<FirstNameRef>) flatFirstNameList(firstNameRef);
		List<SurnameRef> flatSurnameRef = (List<SurnameRef>) flatFirstNameList(surnameRef);

		List<People> peoples = new ArrayList<>(nbPeople);

		IntStream.range(0, nbPeople).forEach(idx -> {
			People people = new People();
			FirstNameRef firstName = flatFirstNameRef.get((int) Math.round(Math.random() * flatFirstNameRef.size()));
			SurnameRef surname = flatSurnameRef.get((int) Math.round(Math.random() * flatFirstNameRef.size()));

			people.setSex(firstName.getSex());
			people.setName(firstName.getName());
			people.setSurname(surname.getName());
			people.setEmail(people.getName().toLowerCase() + "." + people.getSurname().toLowerCase() + "@proxiad.fr");
			people.setPhone(generatePhone());
			people.setBirthDate(generateBirthdayDate());
			people.setArrivalDate(generateArrivalDate());
			people.setCity(CITIES.get((int) Math.floor(Math.random() * CITIES.size())));
			people.setPictureIndex((int) Math.round(Math.random() * MAP_MAX_PICTURE_INDEX_BY_SEX.get(people.getSex())));

			peoples.add(people);
		});

		return peoples;
	}

	private List<? extends NameRef> flatFirstNameList(List<? extends NameRef> firstNameForOneSex) {
		List<NameRef> firstNameList = new ArrayList<>();

		firstNameForOneSex.forEach(firstNameRef -> IntStream.range(0, firstNameRef.getNombre() / 100).forEach(idx ->
				firstNameList.add(firstNameRef)));

		return firstNameList;
	}

	private String generatePhone() {
		return Math.random() < 0.75 ? "06." : "07." +
				IntStream.range(0, 4)
						.mapToObj(idx -> String.format("%05d", Math.round(Math.random() * 99)))
						.collect(Collectors.joining("."));
	}

	private LocalDateTime generateBirthdayDate() {
		return this.birthdayMinDate.plusDays(Math.round(Math.random() * 365 * 20));
	}

	private LocalDateTime generateArrivalDate() {
		return this.arrivalMaxDate.minusDays(Math.round(Math.random() * 365 * 15));
	}

	private class NameRef {
		@Getter
		protected String name;
		@Getter
		protected Integer nombre;
	}

	private class FirstNameRef extends NameRef {
		@Getter
		private Integer sex;

		FirstNameRef(String line) {
			this.sex = Integer.valueOf(line.split("\t")[0].trim());
			this.name = line.split("\t")[1].trim();
			this.nombre = Integer.valueOf(line.split("\t")[2].trim());
		}
	}

	private class SurnameRef extends NameRef {
		@Getter
		private String name;
		@Getter
		private Integer nombre;

		SurnameRef(String line) {
			this.name = line.split("\t")[0].trim();
			this.nombre = Integer.valueOf(line.split("\t")[1].trim());
		}
	}

}
