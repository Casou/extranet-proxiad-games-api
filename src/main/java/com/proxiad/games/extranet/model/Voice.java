package com.proxiad.games.extranet.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="voice")
@NoArgsConstructor
public class Voice {

	@Id
	private String name;

	private Double pitch = 1.0;
	private Double rate = 1.0;
	private Double volume = 1.0;

}
