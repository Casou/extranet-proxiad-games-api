package com.proxiad.games.extranet.model;

import javax.persistence.*;
import java.time.LocalDateTime;

import org.springframework.lang.NonNull;

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

}
