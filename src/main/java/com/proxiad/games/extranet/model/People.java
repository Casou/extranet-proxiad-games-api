package com.proxiad.games.extranet.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import org.springframework.lang.NonNull;

import com.proxiad.games.extranet.enums.LanguageEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="people")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class People {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NonNull
	private String name;

	@NonNull
	private String surname;

	private Integer sex;

	private LocalDateTime birthDate;
	private LocalDateTime arrivalDate;
	private String email;
	private String phone;
	private String city;
	private Integer pictureIndex;
	private String workPlace;

	@Column(name = "languages")
	@ElementCollection
	@Enumerated(EnumType.STRING)
	private Set<LanguageEnum> languages;

	@ElementCollection
	private Set<String> interets;

	@ElementCollection // this is a collection of primitives
	@MapKeyColumn(name="domain") // column name for map "key"
	@Column(name="skill") // column name for map "value"
	private Map<String, String> skills;

}
