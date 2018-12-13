package com.proxiad.games.extranet.dto;

import com.proxiad.games.extranet.model.People;

import lombok.Data;

@Data
public class PeopleNameDto {

	private String name;
	private String surname;

	public PeopleNameDto(People people) {
		this.name = people.getName();
		this.surname = people.getSurname();
	}

}
