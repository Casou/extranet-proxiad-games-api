package com.proxiad.games.extranet.model;

import javax.persistence.*;
import java.time.LocalDateTime;

import com.proxiad.games.extranet.enums.TimerStatusEnum;

import lombok.Data;

@Data
@Table(name="room_timer")
@Entity
public class Timer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private TimerStatusEnum status;
	private LocalDateTime startTime;
	private Integer remainingTime;

}
