package com.proxiad.games.extranet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.annotation.AdminTokenSecurity;
import com.proxiad.games.extranet.model.People;
import com.proxiad.games.extranet.service.PeopleService;

@RestController
public class GeneratePeopleController {

	@Autowired
	private PeopleService peopleService;

	@GetMapping("/admin/generatePeople/{number}")
	@AdminTokenSecurity
	public ResponseEntity<?> generatePeople(@PathVariable("number") Integer numberOfPeople) {
		List<People> people = peopleService.generateRandomPeople(numberOfPeople);
		peopleService.generateIAEntry();
		return new ResponseEntity<>(people.size() + " profiles randomly generated (+ IA profile)", HttpStatus.OK);
	}

}
