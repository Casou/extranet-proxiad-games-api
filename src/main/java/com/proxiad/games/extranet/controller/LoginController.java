package com.proxiad.games.extranet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.annotation.BypassSecurity;
import com.proxiad.games.extranet.dto.LoginAccessDto;
import com.proxiad.games.extranet.dto.LoginDto;
import com.proxiad.games.extranet.model.Token;
import com.proxiad.games.extranet.repository.TokenRepository;
import com.proxiad.games.extranet.utils.SecurityUtils;
import com.proxiad.games.extranet.utils.StringUtils;

import static org.springframework.util.StringUtils.isEmpty;

@RestController
public class LoginController {

	@Value("${extranet.password}")
	private String goodPassword;

	@Value("${extranet.rooms}")
	private String rooms;

	@Autowired
	private TokenRepository tokenRepository;

	@PostMapping("/login")
	@BypassSecurity
	public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
		if (loginDto == null
				|| isEmpty(loginDto.getLogin())) {
			return new ResponseEntity<>("Missing argument(s)", HttpStatus.BAD_REQUEST);
		}

		if (isEmpty(loginDto.getPassword())
				|| !loginDto.getPassword().toLowerCase().equals(goodPassword)) {
			return new ResponseEntity<>("Access denied - Wrong user name / password", HttpStatus.UNAUTHORIZED);
		}

		LoginAccessDto loginAccessDto = LoginAccessDto.builder()
				.token(SecurityUtils.generateToken())
				.build();

		tokenRepository.save(new Token(loginAccessDto.getToken(), StringUtils.capitalize(loginDto.getLogin())));

		return new ResponseEntity<>(loginAccessDto, HttpStatus.OK);
	}

}
