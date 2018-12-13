package com.proxiad.games.extranet.utils;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;

public class GeneratePeopleReferential {

	private static final String SURNAME_FILE = "bdd_noms.txt";
	private static final String FIRSTNAME_FILE = "bdd_prenoms.txt";
	public static final String SURNAME_REFERENTIAL = "src/main/resources/referentiel_noms.txt";
	public static final String FIRSTNAME_REFERENTIAL = "src/main/resources/referentiel_prenoms.txt";

	public static void main(String[] args) {
		generateFirstNameReferential();
		generateSurnameReferential();
	}

	private static void generateSurnameReferential() {
		GeneratePeopleReferential generator = new GeneratePeopleReferential();
		List<SurnamePeople> nomMap = generator.extractSurnamesFromFile();
		File prenomFile = new File(SURNAME_REFERENTIAL);
		List<String> prenoms = nomMap.stream()
				.map(personPrenom -> personPrenom.getName() + "\t" + personPrenom.getNombre())
				.collect(Collectors.toList());

		FileUtils.writeInFile(prenomFile, prenoms);
	}

	private static void generateFirstNameReferential() {
		GeneratePeopleReferential generator = new GeneratePeopleReferential();
		Map<Integer, List<FirstNamePeople>> prenomMap = generator.extractFirstNamesFromFile();
		File prenomFile = new File(FIRSTNAME_REFERENTIAL);
		List<String> prenoms = prenomMap.keySet().stream().map(sex -> prenomMap.get(sex).stream()
				.map(personPrenom -> personPrenom.getSex() + "\t" + personPrenom.getName() + "\t" + personPrenom.getNombre())
				.collect(Collectors.toList()))
				.flatMap(List::stream)
				.collect(Collectors.toList());

		FileUtils.writeInFile(prenomFile, prenoms);
	}

	private List<SurnamePeople> extractSurnamesFromFile() {
		List<String> lines = getFileContent(SURNAME_FILE);

		return lines.stream()
				.map(SurnamePeople::new)
				.filter(people -> people.getNombre() >= 600)
				.collect(Collectors.toList());
	}

	private Map<Integer, List<FirstNamePeople>> extractFirstNamesFromFile() {
		List<String> lines = getFileContent(FIRSTNAME_FILE);

		return lines.stream()
				.map(FirstNamePeople::new)
				.filter(people -> people.getNombre() >= 300)
				.sorted(Comparator.comparing(FirstNamePeople::getName))
				.distinct()
				.collect(Collectors.groupingBy(FirstNamePeople::getSex, Collectors.toList()));
	}

	private List<String> getFileContent(String nameFile) {
		File file = getFile(nameFile);
		List<String> lines = FileUtils.readFile(file);
		lines.remove(0);
		return lines;
	}


	private File getFile(String nameFile) {
		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		return new File(classLoader.getResource(nameFile).getFile());
	}

	private class FirstNamePeople {
		@Getter
		private Integer sex;
		@Getter
		private String name;
		@Getter
		private Integer nombre;

		FirstNamePeople(String line) {
			this.sex = Integer.valueOf(line.split("\t")[0].trim());
			this.name = line.split("\t")[1].trim();
			this.nombre = Integer.valueOf(line.split("\t")[3].trim());
		}

		@Override
		public boolean equals(Object o) {
			return name.equals(((FirstNamePeople) o).getName());
		}

		@Override
		public int hashCode() {
			return this.getName().hashCode();
		}

	}

	private class SurnamePeople {
		@Getter
		private String name;
		@Getter
		private Integer nombre;

		SurnamePeople(String line) {
			this.name = line.split("\t")[0].trim();
			this.nombre = Integer.valueOf(line.split("\t")[11].trim());
		}

		@Override
		public boolean equals(Object o) {
			return name.equals(((SurnamePeople) o).getName());
		}

		@Override
		public int hashCode() {
			return this.getName().hashCode();
		}
	}


}
