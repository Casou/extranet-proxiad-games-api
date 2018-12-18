package com.proxiad.games.extranet.utils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.proxiad.games.extranet.enums.LanguageEnum;
import com.proxiad.games.extranet.model.People;

import lombok.Getter;

@Component
public class PeopleGenerator {

	private static final Map<Integer, Integer> MAP_MAX_PICTURE_INDEX_BY_SEX = new HashMap<Integer, Integer>() {{
		put(1, 26);
		put(2, 26);
	}};
	private static final List<String> CITIES = Arrays.asList("Lille", "Paris", "Bordeaux", "Nantes", "Rouen",
			"Aix en Provence", "Bulgarie", "Strasbourg", "Lyon");

	private static final List<String> INTERETS = Arrays.asList("Badminton", "Volley", "Football", "Echecs", "Jeux de société", "Photographie",
			"Musique", "Cinéma", "Voyages", "Livres", "Jeux vidéos", "Escalade", "Cuisine", "TCG", "Comics",
			"Bénévolat", "Poker", "Barbecue", "Dessin", "Peinture", "Karaoké", "Billard", "Babyfoot", "Fléchettes",
			"Danse", "Concerts", "Musée", "Poterie", "Modélisme", "Philatélie", "Cosplay", "Couture", "Tricot");

	private static final List<String> CLIENTS = Arrays.asList("Orange (Lille)", "Orange (V2)", "JCDecaux", "Technip", "Auchan",
			"Europcar", "MAE", "Cofidis", "Antargaz", "Boursorama", "Crédit agricole", "Natixis", "Société générale", "SNCF",
			"Maif", "MGEN", "Adeo", "Leroy Merlin", "Easy Voyage", "Banque Accor", "Dexia", "Axa", "Ferrero", "Caisse d'épargne",
			"Airfrance", "Alstom", "Humanis", "Groupama", "SAP", "BNP", "AG2R", "Ariba", "France Television", "GSK", "Banque Populaire",
			"Pay-on", "Illicado", "Harmonie mutuelle");

	private static Map<String, List<String>> SKILLS = new HashMap<>();

	private LocalDateTime birthdayMinDate;
	private LocalDateTime arrivalMaxDate;

	PeopleGenerator() {
		this.birthdayMinDate = LocalDateTime.of(1975, Month.JANUARY, 1, 0, 0);
		this.arrivalMaxDate = LocalDateTime.now().minusMonths(3);
		initSkillsMap();
	}

	private void initSkillsMap() {
		SKILLS.put("Backend Technology", Arrays.asList("Spring", "Spring Boot", "Hibernate", "Struts", "Maven"));
		SKILLS.put("Frontend Technology", Arrays.asList("React", "Angular", "AngularJS", "Ember", "Vue.js", "Node", "Bootstrap", "Webpack", "Babel"));
		SKILLS.put("Databases and Server", Arrays.asList("Oracle", "Postgresql", "MySQL", "H2", "Nosql", "Plex", "Oracle Applications", "Firebase"));
		SKILLS.put("Integration tools", Arrays.asList("Jenkins", "Hudson", "Nexus"));
		SKILLS.put("Graphical tools", Arrays.asList("Gimp", "Photoshop", "Illustrator", "InDesign", "Lightroom", "Affinity", "After Effect"));
		SKILLS.put("Methodology", Arrays.asList("Agile", "ITIL", "Lean", "Extreme programming", "Crystal programming", "SAFe"));
	}

	public List<People> generatePeople(Integer nbPeople) {
		File firstNameReferential = new File(PeopleReferentialGenerator.FIRSTNAME_REFERENTIAL);
		File surnameReferential = new File(PeopleReferentialGenerator.SURNAME_REFERENTIAL);

		List<FirstNameRef> firstNameRef = FileUtils.readFile(firstNameReferential).stream().map(FirstNameRef::new).collect(Collectors.toList());
		List<SurnameRef> surnameRef = FileUtils.readFile(surnameReferential).stream().map(SurnameRef::new).collect(Collectors.toList());

		List<FirstNameRef> flatFirstNameRef = (List<FirstNameRef>) flatFirstNameList(firstNameRef);
		List<SurnameRef> flatSurnameRef = (List<SurnameRef>) flatFirstNameList(surnameRef);

		List<People> peoples = new ArrayList<>(nbPeople);

		IntStream.range(0, nbPeople).forEach(idx -> {
			People people = new People();
			FirstNameRef firstName = flatFirstNameRef.get((int) Math.round(Math.random() * (flatFirstNameRef.size() - 1)));
			SurnameRef surname = flatSurnameRef.get((int) Math.round(Math.random() * (flatFirstNameRef.size() - 1)));

			while (alreadyExist(peoples, surname.getName(), firstName.getName())) {
				firstName = flatFirstNameRef.get((int) Math.round(Math.random() * (flatFirstNameRef.size() - 1)));
				surname = flatSurnameRef.get((int) Math.round(Math.random() * (flatFirstNameRef.size() - 1)));
			}

			people.setSex(firstName.getSex());
			people.setName(StringUtils.capitalize(firstName.getName()));
			people.setSurname(StringUtils.capitalize(surname.getName()));
			people.setEmail(people.getName().toLowerCase() + "." + people.getSurname().toLowerCase() + "@proxiad.fr");
			people.setPhone(generatePhone());
			people.setBirthDate(generateBirthdayDate());
			people.setArrivalDate(generateArrivalDate());
			people.setCity(CITIES.get((int) Math.floor(Math.random() * CITIES.size())));
			people.setPictureIndex((int) Math.round(Math.random() * (MAP_MAX_PICTURE_INDEX_BY_SEX.get(people.getSex()) - 1)) + 1);
			people.setLanguages(generateLanguages());
			people.setInterets(generateInterets());
			people.setSkills(generateSkills());
			people.setWorkPlace(CollectionUtils.random(CLIENTS));

			peoples.add(people);
		});

		return peoples;
	}

	private boolean alreadyExist(List<People> peoples, String surname, String firstName) {
		return peoples.stream().anyMatch(p -> p.getSurname().equals(surname) && p.getName().equals(firstName));
	}

	private List<? extends NameRef> flatFirstNameList(List<? extends NameRef> firstNameForOneSex) {
		List<NameRef> firstNameList = new ArrayList<>();

		firstNameForOneSex.forEach(firstNameRef -> IntStream.range(0, firstNameRef.getNombre() / 100).forEach(idx ->
				firstNameList.add(firstNameRef)));

		return firstNameList;
	}

	private String generatePhone() {
		return (Math.random() < 0.75 ? "06." : "07.") +
				IntStream.range(0, 4)
						.mapToObj(idx -> String.format("%02d", Math.round(Math.random() * 99)))
						.collect(Collectors.joining("."));
	}

	private LocalDateTime generateBirthdayDate() {
		return this.birthdayMinDate.plusDays(Math.round(Math.random() * 365 * 20));
	}

	private LocalDateTime generateArrivalDate() {
		return this.arrivalMaxDate.minusDays(Math.round(Math.random() * 365 * 15));
	}

	private Set<LanguageEnum> generateLanguages() {
		Set<LanguageEnum> languages = new HashSet<>();
		languages.add(LanguageEnum.FRANCAIS);
		while (Math.random() > 0.5 && languages.size() <= 3) {
			languages.add(LanguageEnum.list().get((int) Math.round(Math.random() * (LanguageEnum.list().size() - 1))));
		}
		return languages;
	}

	private Set<String> generateInterets() {
		Set<String> interets = new HashSet<>();
		while (Math.random() > 0.35 && interets.size() < 6) {
			interets.add(INTERETS.get((int) Math.round(Math.random() * (INTERETS.size() - 1))));
		}
		return interets;
	}

	private Map<String, String> generateSkills() {
		Map<String, String> skills = new HashMap<>();

		IntStream.range(0, 3).forEach(idx -> {
			Map.Entry<String, List<String>> skill = CollectionUtils.random(SKILLS.entrySet());
			skills.put(skill.getKey(),
					CollectionUtils.random(skill.getValue(), 2 + (int) Math.round(Math.random() * 3)) // 2-4 skills by domain
						.stream()
						.distinct()
						.collect(Collectors.joining(",")));
		});

		return skills;
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
