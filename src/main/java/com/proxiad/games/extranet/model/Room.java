package com.proxiad.games.extranet.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="room")
@NoArgsConstructor
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotNull
	private String name;

	@OneToOne(fetch = FetchType.LAZY, orphanRemoval=true, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "token_id")
	private Token connectedToken;

	@OneToMany(orphanRemoval=true, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "room_id")
	private List<Riddle> riddles = new ArrayList<>();

	@OneToOne(fetch = FetchType.EAGER, orphanRemoval=true, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "timer_id")
	private Timer timer;

	private Boolean isTerminated = false;
	private String terminateStatus;

	@NotNull
	private Integer trollIndex = 0;

	@NotNull
	private Double audioBackgroundVolume = 0.0;

	public boolean containsRiddle(String riddleId) {
		return riddles.stream().anyMatch(r -> r.getRiddleId().equals(riddleId));
	}

}
