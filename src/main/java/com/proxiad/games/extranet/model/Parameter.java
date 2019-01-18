package com.proxiad.games.extranet.model;

import javax.persistence.*;

import org.springframework.lang.NonNull;

import lombok.Data;

@Data
@Entity
@Table(name="people")
public class Parameter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NonNull
	private String key;

	@NonNull
	private String value;

}
