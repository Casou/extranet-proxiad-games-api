package com.proxiad.games.extranet.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proxiad.games.extranet.dto.TokenDto;
import com.proxiad.games.extranet.model.Token;
import com.proxiad.games.extranet.repository.TokenRepository;

@Service
public class AuthService {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private TokenRepository tokenRepository;

	private static ModelMapper mapper = new ModelMapper();

	public Optional<TokenDto> validateToken(String token) {
		if (token == null) {
			return Optional.empty();
		}

		TokenDto tokenDto = this.tokenService.findById(token);
		if (tokenDto == null || !tokenDto.isValid()) {
			return Optional.empty();
		}

		tokenDto.refreshExpirationDate();

		Token tokenEntity = mapper.map(tokenDto, Token.class);
		tokenEntity.setExpirationDate(tokenDto.getExpirationDate());
		tokenRepository.save(tokenEntity);

		return Optional.of(tokenDto);
	}

}
