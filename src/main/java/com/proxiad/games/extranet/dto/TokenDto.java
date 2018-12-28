package com.proxiad.games.extranet.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TokenDto {

	private String token;
	private LocalDateTime expirationDate;

	public boolean isValid() {
		return LocalDateTime.now().isBefore(this.expirationDate);
	}

	public void refreshExpirationDate() {
		this.expirationDate = LocalDateTime.now().plusHours(1);
	}

}
