package com.proxiad.games.extranet.utils;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.proxiad.games.extranet.model.People;

import lombok.Getter;

public class GeneratePeople {

	private static final String SURNAME_FILE = "bdd_noms.txt";
	private static final String NAME_FILE = "bdd_prenoms.txt";


	public static List<People> generatePeople(Integer nbPeople) {

	}

	public static void main(String[] args) {
		generateFirstNameReferential();
		generateSurnameReferential();
	}

	private static void generateSurnameReferential() {
		GeneratePeople generator = new GeneratePeople();
		List<FirstNamePeople> nomMap = generator.extractSurnamesFromFile();
		File prenomFile = new File("src/main/resources/referentiel_noms.txt");
		List<String> prenoms = nomMap.stream()
				.map(personPrenom -> personPrenom.getName() + "\t" + personPrenom.getNombre())
				.collect(Collectors.toList());

		FileUtils.writeInFile(prenomFile, prenoms);
	}

	private static void generateFirstNameReferential() {
		GeneratePeople generator = new GeneratePeople();
		Map<Integer, List<FirstNamePeople>> prenomMap = generator.extractFirstNamesFromFile();
		File prenomFile = new File("src/main/resources/referentiel_prenoms.txt");
		List<String> prenoms = prenomMap.keySet().stream().map(sex -> prenomMap.get(sex).stream()
				.map(personPrenom -> personPrenom.getSex() + "\t" + personPrenom.getName() + "\t" + personPrenom.getNombre())
				.collect(Collectors.toList()))
				.flatMap(List::stream)
				.collect(Collectors.toList());

		FileUtils.writeInFile(prenomFile, prenoms);
	}

	private List<FirstNamePeople> extractSurnamesFromFile() {
		//Get file from resources folder
		List<String> lines = getFileContent(SURNAME_FILE);

		return lines.stream()
				.map(line -> new FirstNamePeople(
						null,
						line.split("\t")[0].trim(),
						Integer.valueOf(line.split("\t")[11].trim())
				))
				.filter(people -> people.getNombre() >= 600)
//				.filter(name -> name.length() > 4 && name.charAt(0) != name.charAt(1))
//				.sorted()
				.collect(Collectors.toList());
	}

	private Map<Integer, List<FirstNamePeople>> extractFirstNamesFromFile() {
		List<String> lines = getFileContent(NAME_FILE);

		return lines.stream()
				.map(line -> new FirstNamePeople(
						Integer.valueOf(line.split("\t")[0].trim()),
						line.split("\t")[1].trim(),
						Integer.valueOf(line.split("\t")[3].trim())
				))
				.filter(people -> people.getNombre() >= 300)
//				.filter(people -> people.getName().length() > 4
//						&& people.getName().charAt(0) != people.getName().charAt(1))
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

	class FirstNamePeople {
		@Getter
		private Integer sex;
		@Getter
		private String name;
		@Getter
		private Integer nombre;

		FirstNamePeople(Integer sex, String name, Integer nombre) {
			this.sex = sex;
			this.name = name;
			this.nombre = nombre;
		}

		@Override
		public String toString() {
			return this.getSex() + " " + this.getName();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			FirstNamePeople people = (FirstNamePeople) o;
			return name.equals(people.getName());
		}

		@Override
		public int hashCode() {
			return this.name.hashCode();
		}
	}


}
