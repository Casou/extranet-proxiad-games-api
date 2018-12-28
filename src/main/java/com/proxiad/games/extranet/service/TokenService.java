package com.proxiad.games.extranet.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proxiad.games.extranet.dto.TokenDto;
import com.proxiad.games.extranet.model.Token;
import com.proxiad.games.extranet.repository.TokenRepository;

@Service
public class TokenService {

	@Autowired
	private TokenRepository tokenRepository;

	private static ModelMapper mapper = new ModelMapper();

	public TokenDto findById(String token) {
		Optional<Token> optToken = this.tokenRepository.findById(token);
		return optToken.map(token1 -> mapper.map(token1, TokenDto.class))
				.orElse(null);
	}
}
