package com.proxiad.games.extranet.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.proxiad.games.extranet.enums.TextEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="text")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Text {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotNull
	private String text = "";

	@NotNull
	private String voice = "French Female";

	@Enumerated(EnumType.STRING)
	@NotNull
	private TextEnum discriminant;

	private String videoName;

	public Text(TextEnum discriminant) {
		this.discriminant = discriminant;
	}

}
