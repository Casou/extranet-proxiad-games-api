package com.proxiad.games.extranet.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.proxiad.games.extranet.enums.LanguageEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeopleDto {

	private Integer id;
	private String name;
	private String surname;
	private Integer sex;
	private LocalDateTime birthDate;
	private LocalDateTime arrivalDate;
	private String email;
	private String phone;
	private String city;
	private Integer pictureIndex;
	private Set<LanguageEnum> languages;
	private Set<String> interets;
	private String workPlace;

	private Integer age;
	private Map<String, List<String>> skillMap;

}
