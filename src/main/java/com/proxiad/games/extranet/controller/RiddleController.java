package com.proxiad.games.extranet.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.proxiad.games.extranet.annotation.AdminTokenSecurity;
import com.proxiad.games.extranet.model.Riddle;
import com.proxiad.games.extranet.repository.RiddleRepository;

@RestController
public class RiddleController {

	@Autowired
	private RiddleRepository riddleRepository;

	@GetMapping("/riddles")
	@AdminTokenSecurity
	public List<Riddle> getAllRiddles() {
		return riddleRepository.findAll();
	}

	@PostMapping("/riddle")
	@AdminTokenSecurity
	public void saveRiddle(@RequestBody Riddle riddle) {
		riddleRepository.save(riddle);
	}

	@DeleteMapping("/riddle")
	@AdminTokenSecurity
	public void deleteRiddle(@RequestBody Riddle riddle) {
		Optional<Riddle> optRiddle = riddleRepository.findById(riddle.getId());

		if (!optRiddle.isPresent()) {
			throw new RuntimeException("Riddle " + riddle.getId() + " not found");
		}

		riddleRepository.delete(optRiddle.get());
	}

}
