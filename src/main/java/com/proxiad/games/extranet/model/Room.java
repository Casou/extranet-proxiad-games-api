package com.proxiad.games.extranet.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@Table(name="room")
@NoArgsConstructor
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NonNull
	private String name;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "token_id")
	private Token connectedToken;

	@Column(name = "resolved_riddles")
	@ElementCollection(fetch =  FetchType.EAGER)
	private List<Riddle> resolvedRiddles = new ArrayList<>();

	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name = "timer_id")
	private Timer timer;

	private Boolean isTerminated = false;
	private String terminateStatus;

	public boolean containsRiddle(String riddleId) {
		return resolvedRiddles.stream().anyMatch(r -> r.getRiddleId().equals(riddleId));
	}

}
