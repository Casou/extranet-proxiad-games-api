package com.proxiad.games.extranet.model;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="intro_sentence")
@NoArgsConstructor
public class IntroSentence {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String text = "";
	private String voice = "Google français";

}
