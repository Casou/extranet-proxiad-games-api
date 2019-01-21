package com.proxiad.games.extranet.model;

import javax.persistence.*;

import org.springframework.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="parameter")
@NoArgsConstructor
public class Parameter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NonNull
	private String key;

	private String description;

	@NonNull
	private String value;

}
