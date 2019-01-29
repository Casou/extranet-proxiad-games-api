package com.proxiad.games.extranet.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.proxiad.games.extranet.enums.TextEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="text")
@NoArgsConstructor
public class Text {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotNull
	private String text = "";

	@NotNull
	private String voice = "Google français";

	@Enumerated(EnumType.STRING)
	@NotNull
	private TextEnum discriminant;

	public Text(TextEnum discriminant) {
		this.discriminant = discriminant;
	}

}
