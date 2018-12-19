package com.proxiad.games.extranet.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proxiad.games.extranet.dto.UnlockDto;
import com.proxiad.games.extranet.model.UnlockedRiddle;
import com.proxiad.games.extranet.repository.UnlockedRiddleRepository;

@RestController
public class UnlockController {

	@Autowired
	private UnlockedRiddleRepository unlockedRiddleRepository;

	public static final Map<String, String> RIDDLE_RESPONSES = new HashMap<String, String>() {{
		put("riddle1", "abcde");
		put("riddle2", "zzzzz");
		put("riddle3", "aaaaa");
	}};

	@GetMapping("/unlock/status")
	public List<String> getUnlockStatus(@RequestAttribute String token) {
		return unlockedRiddleRepository.findByTokenUser(token).stream()
				.map(UnlockedRiddle::getRiddleId)
				.collect(Collectors.toList());
	}

	@PostMapping("/unlock")
	public ResponseEntity<?> unlockBolt(@RequestBody UnlockDto unlockDto, @RequestAttribute String token) {
		if (!RIDDLE_RESPONSES.containsKey(unlockDto.getId()) || !RIDDLE_RESPONSES.get(unlockDto.getId()).equals(unlockDto.getPassword())){
			return new ResponseEntity<>("Id and password don't match.", HttpStatus.FORBIDDEN);
		}

		List<UnlockedRiddle> resolvedRiddles = unlockedRiddleRepository.findByTokenUser(token);
		if (resolvedRiddles.stream().anyMatch(riddle -> riddle.getRiddleId().equals(unlockDto.getId()))) {
			return new ResponseEntity<>("Riddle already unlocked.", HttpStatus.FORBIDDEN);
		}

		UnlockedRiddle riddle = new UnlockedRiddle();
		riddle.setRiddleId(unlockDto.getId());
		riddle.setTokenUser(token);
		unlockedRiddleRepository.save(riddle);

		return new ResponseEntity<>("unlocked", HttpStatus.OK);
	}

}
