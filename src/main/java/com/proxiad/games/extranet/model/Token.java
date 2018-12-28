package com.proxiad.games.extranet.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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

//	private List<UnlockedRiddle> unlockedRiddles;

	public Token(String token, String userName) {
		this(token, userName, LocalDateTime.now().plusHours(1));
	}

	public Token(String token, String userName, LocalDateTime expirationDate) {
		this.token = token;
		this.expirationDate = expirationDate;
	}

}
