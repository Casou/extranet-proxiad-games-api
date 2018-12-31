package com.proxiad.games.extranet.model;

import javax.persistence.*;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@Table(name="security_token")
@NoArgsConstructor
public class Token {

	@Id
	private String token;
	@NonNull
	private String roomName;
	@NonNull
	private LocalDateTime expirationDate;

//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "room_id")
//	private Room room;

	public Token(String token) {
		this(token, LocalDateTime.now().plusHours(1));
	}

	public Token(String token, LocalDateTime expirationDate) {
		this.token = token;
		this.expirationDate = expirationDate;
	}

}
