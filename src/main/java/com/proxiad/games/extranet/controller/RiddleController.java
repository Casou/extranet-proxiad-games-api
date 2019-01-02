package com.proxiad.games.extranet.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proxiad.games.extranet.annotation.AdminTokenSecurity;
import com.proxiad.games.extranet.dto.RiddleDto;
import com.proxiad.games.extranet.exception.ProxiadControllerException;
import com.proxiad.games.extranet.model.Riddle;
import com.proxiad.games.extranet.repository.RiddleRepository;

@RestController
@CrossOrigin
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

	@PutMapping(value = "/riddle")
	@AdminTokenSecurity
	public ResponseEntity<?> newRiddle() {
		Riddle riddle = new Riddle();
		riddle.setName("Nouveau");
		riddle.setRiddleId("idToSet");
		riddle.setRiddlePassword("passwordToSet");
		riddleRepository.save(riddle);

		return new ResponseEntity<>(riddle, HttpStatus.OK);
	}

	@PostMapping(value = "/riddle/{id}/name")
	@AdminTokenSecurity
	public ResponseEntity<?> updateRiddleName(@PathVariable("id") Integer id, @RequestBody RiddleDto riddleDto) {
		Riddle riddle;
		try {
			riddle = getRiddle(id);
		} catch (ProxiadControllerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		riddle.setName(riddleDto.getName());
		riddleRepository.save(riddle);

		return new ResponseEntity<>(riddle, HttpStatus.OK);
	}

	@PostMapping(value = "/riddle/{id}/riddleId")
	@AdminTokenSecurity
	public ResponseEntity<?> updateRiddleId(@PathVariable("id") Integer id, @RequestBody RiddleDto riddleDto) {
		Riddle riddle;
		try {
			riddle = getRiddle(id);
		} catch (ProxiadControllerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		riddle.setRiddleId(riddleDto.getRiddleId());
		riddleRepository.save(riddle);

		return new ResponseEntity<>(riddle, HttpStatus.OK);
	}

	@PostMapping(value = "/riddle/{id}/riddlePassword")
	@AdminTokenSecurity
	public ResponseEntity<?> updateRiddlePasswor(@PathVariable("id") Integer id, @RequestBody RiddleDto riddleDto) {
		Riddle riddle;
		try {
			riddle = getRiddle(id);
		} catch (ProxiadControllerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		riddle.setRiddlePassword(riddleDto.getRiddlePassword());
		riddleRepository.save(riddle);

		return new ResponseEntity<>(riddle, HttpStatus.OK);
	}

	private Riddle getRiddle(Integer id) throws ProxiadControllerException {
		Optional<Riddle> optRiddle = riddleRepository.findById(id);

		if (!optRiddle.isPresent()) {
			throw new ProxiadControllerException("No riddle with id " + id);
		}

		return optRiddle.get();
	}

	@DeleteMapping(value = "/riddle/{id}")
	@AdminTokenSecurity
	public ResponseEntity<?> deleteRiddle(@PathVariable("id") Integer id) {
		Riddle riddle;
		try {
			riddle = getRiddle(id);
		} catch (ProxiadControllerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		riddleRepository.delete(riddle);

		return new ResponseEntity<>("deleted", HttpStatus.OK);
	}

}
