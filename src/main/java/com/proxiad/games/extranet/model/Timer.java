package com.proxiad.games.extranet.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.proxiad.games.extranet.enums.TimerStatusEnum;

import lombok.Data;

@Data
@Table(name="room_timer")
@Entity
public class Timer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Enumerated(EnumType.STRING)
	private TimerStatusEnum status;

	private LocalDateTime serverStartTime;
	private String clientStartTime;
	private Integer remainingTime;

	public Integer calculatedRemainingTime() {
		final long remainingTime = this.getRemainingTime() - this.getServerStartTime().until(LocalDateTime.now(), ChronoUnit.SECONDS);
		return Math.max(0, Math.toIntExact(remainingTime));
	}

}
