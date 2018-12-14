package com.proxiad.games.extranet.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="security_token")
@NoArgsConstructor
public class Token {

	@Id
	private String token;
	private LocalDateTime expirationDate;

	public Token(String token) {
		this(token, LocalDateTime.now().plusHours(1));
	}

	public Token(String token, LocalDateTime expirationDate) {
		this.token = token;
		this.expirationDate = expirationDate;
	}

}
