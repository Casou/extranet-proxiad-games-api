package com.proxiad.games.extranet.dto;

import com.proxiad.games.extranet.model.People;

import lombok.Data;

@Data
public class PeopleNameDto {

	private Integer id;
	private String name;
	private String surname;
	private String city;
	private Integer pictureIndex;
	private String sex;

	public PeopleNameDto(People people) {
		this.id = people.getId();
		this.name = people.getName();
		this.surname = people.getSurname();
		this.city = people.getCity();
		this.pictureIndex = people.getPictureIndex();
		this.sex = people.getSex() == 1 ? "male" : "female";
	}

}
