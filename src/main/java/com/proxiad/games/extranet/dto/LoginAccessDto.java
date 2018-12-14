package com.proxiad.games.extranet.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginAccessDto {

	private String token;

}
