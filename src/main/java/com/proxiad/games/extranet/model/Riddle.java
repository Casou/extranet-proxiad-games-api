package com.proxiad.games.extranet.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proxiad.games.extranet.enums.RiddleType;

import lombok.*;

@Data
@Entity
@Table(name="riddle")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Riddle {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NonNull
	private String name;

	@NonNull
	private String riddleId;

	private String riddlePassword = null;

	@NonNull
	private Boolean resolved = false;

	@NonNull
	private RiddleType type;

	@ManyToOne
	@JoinColumn(name="room_id", nullable=false)
	@JsonIgnore
	@ToString.Exclude
	private Room room;

}
