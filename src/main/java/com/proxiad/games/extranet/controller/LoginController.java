package com.proxiad.games.extranet.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.annotation.BypassSecurity;
import com.proxiad.games.extranet.dto.LoginAccessDto;
import com.proxiad.games.extranet.dto.LoginDto;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.model.Token;
import com.proxiad.games.extranet.repository.RoomRepository;
import com.proxiad.games.extranet.repository.TokenRepository;
import com.proxiad.games.extranet.utils.SecurityUtils;
import com.proxiad.games.extranet.utils.StringUtils;

import static org.springframework.util.StringUtils.isEmpty;

@RestController
@CrossOrigin
public class LoginController {

	@Value("${extranet.password}")
	private String goodPassword;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private RoomRepository roomRepository;

	@PostMapping("/login")
	@BypassSecurity
	public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
		if (loginDto == null || isEmpty(loginDto.getLogin())) {
			return new ResponseEntity<>("Missing argument(s)", HttpStatus.BAD_REQUEST);
		}

		Optional<Room> optRoom = roomRepository.findByNameIgnoreCase(StringUtils.capitalize(loginDto.getLogin()));
		if (isEmpty(loginDto.getPassword())
				|| !optRoom.isPresent()
				|| !loginDto.getPassword().toLowerCase().equals(goodPassword)) {
			return new ResponseEntity<>("Access denied - Wrong user name / password", HttpStatus.UNAUTHORIZED);
		}

		LoginAccessDto loginAccessDto = LoginAccessDto.builder()
				.token(SecurityUtils.generateToken())
				.build();

		Token token = tokenRepository.save(new Token(loginAccessDto.getToken()));

		Room room = optRoom.get();
		room.setConnectedToken(token);
		roomRepository.save(room);

		return new ResponseEntity<>(loginAccessDto, HttpStatus.OK);
	}

}
