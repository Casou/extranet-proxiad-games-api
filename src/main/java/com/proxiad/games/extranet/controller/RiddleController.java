package com.proxiad.games.extranet.controller;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proxiad.games.extranet.annotation.AdminTokenSecurity;
import com.proxiad.games.extranet.dto.RiddleDto;
import com.proxiad.games.extranet.model.Riddle;
import com.proxiad.games.extranet.repository.RiddleRepository;
import com.proxiad.games.extranet.service.RiddleService;

@RestController
@CrossOrigin
public class RiddleController {

	@Autowired
	private RiddleService riddleService;

	@Autowired
	private RiddleRepository riddleRepository;

	@GetMapping("/riddle")
	@AdminTokenSecurity
	public List<Riddle> getAllRiddles() {
		return riddleRepository.findAll();
	}

	@PutMapping(value = "/riddle")
	@AdminTokenSecurity
	public ResponseEntity<?> newRiddle(@RequestBody RiddleDto riddleDto) {
		return new ResponseEntity<>(riddleService.newRiddle(riddleDto.getRoomId()), HttpStatus.OK);
	}

	@PatchMapping(value = "/riddle/{id}/name")
	@AdminTokenSecurity
	public ResponseEntity<?> updateRiddleName(@PathVariable("id") Integer id, @RequestBody RiddleDto riddleDto) {
		Riddle riddle = riddleRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("No riddle with id " + id));
		riddle.setName(riddleDto.getName());
		riddleRepository.save(riddle);

		return new ResponseEntity<>(riddle, HttpStatus.OK);
	}

	@PatchMapping(value = "/riddle/{id}/riddleId")
	@AdminTokenSecurity
	public ResponseEntity<?> updateRiddleId(@PathVariable("id") Integer id, @RequestBody RiddleDto riddleDto) {
		Riddle riddle = riddleRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("No riddle with id " + id));
		riddle.setRiddleId(riddleDto.getRiddleId());
		riddleRepository.save(riddle);

		return new ResponseEntity<>(riddle, HttpStatus.OK);
	}

	@PatchMapping(value = "/riddle/{id}/riddlePassword")
	@AdminTokenSecurity
	public ResponseEntity<?> updateRiddlePassword(@PathVariable("id") Integer id, @RequestBody RiddleDto riddleDto) {
		Riddle riddle = riddleRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("No riddle with id " + id));
		riddle.setRiddlePassword(riddleDto.getRiddlePassword());
		riddleRepository.save(riddle);

		return new ResponseEntity<>(riddle, HttpStatus.OK);
	}

	@DeleteMapping(value = "/riddle/{id}")
	@AdminTokenSecurity
	public ResponseEntity<?> deleteRiddle(@PathVariable("id") Integer id) {
		Riddle riddle = riddleRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("No riddle with id " + id));
		riddleRepository.delete(riddle);

		return new ResponseEntity<>("deleted", HttpStatus.OK);
	}

}
