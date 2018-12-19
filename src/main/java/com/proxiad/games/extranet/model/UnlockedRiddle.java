package com.proxiad.games.extranet.model;

import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name="unlocked_riddle")
@Data
public class UnlockedRiddle {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String tokenUser;
	private String riddleId;

}
