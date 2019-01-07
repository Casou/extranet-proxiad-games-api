package com.proxiad.games.extranet.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.model.UnlockedRiddle;
import com.proxiad.games.extranet.repository.UnlockedRiddleRepository;

@RestController
@CrossOrigin
public class RedPillController {

	@Autowired
	private UnlockedRiddleRepository unlockedRiddleRepository;

	@GetMapping("/redpill")
	public ResponseEntity<?> applyRedPill(@RequestAttribute String token) {
		List<UnlockedRiddle> resolvedRiddles = unlockedRiddleRepository.findByTokenUser(token);
		List<String> resolvedRiddleIds = resolvedRiddles.stream()
				.map(UnlockedRiddle::getRiddleId)
				.collect(Collectors.toList());

		if (UnlockController.RIDDLE_RESPONSES.keySet().stream().anyMatch(riddleId -> !resolvedRiddleIds.contains(riddleId))) {
			return new ResponseEntity<>("You shouldn't have call the redpill command until all the riddles are resolved.", HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<>("Access granted", HttpStatus.OK);
	}

}
