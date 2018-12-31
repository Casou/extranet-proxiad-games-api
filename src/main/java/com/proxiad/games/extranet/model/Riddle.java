package com.proxiad.games.extranet.model;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@Table(name="riddle")
@NoArgsConstructor
public class Riddle {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NonNull
	private String name;

	@NonNull
	private String riddleId;

	@NonNull
	private String riddlePassword;

}
