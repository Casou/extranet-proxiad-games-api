package com.proxiad.games.extranet.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.dto.UnlockDto;

@RestController
public class UnlockController {

	private static final Map<String, String> RIDDLE_RESPONSES = new HashMap<String, String>() {{
		put("riddle1", "abcde");
		put("riddle2", "zzzzz");
		put("riddle3", "aaaaa");
	}};

	@PostMapping("/unlock")
	public ResponseEntity<?> unlockBolt(@RequestBody UnlockDto unlockDto) {
		if (!RIDDLE_RESPONSES.containsKey(unlockDto.getId()) || !RIDDLE_RESPONSES.get(unlockDto.getId()).equals(unlockDto.getPassword())){
			return new ResponseEntity<>("Id and password don't match.", HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<>("unlocked", HttpStatus.OK);
	}

}
