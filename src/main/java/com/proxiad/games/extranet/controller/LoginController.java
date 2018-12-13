package com.proxiad.games.extranet.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.dto.LoginAccess;
import com.proxiad.games.extranet.utils.SecurityUtils;

@RestController
public class LoginController {

	@Value("${extranet.password}")
	private String goodPassword;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody String password) {
		if (password == null || !password.toLowerCase().equals(goodPassword)) {
			return new ResponseEntity<>("Access denied - Wrong password", HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<>(LoginAccess.builder()
				.token(SecurityUtils.generateToken())
				.build(), HttpStatus.OK);
	}

}
