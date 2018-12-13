package com.proxiad.games.extranet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.service.PeopleService;

@RestController
public class GeneratePeopleController {

	@Autowired
	private PeopleService peopleService;

	@GetMapping("/generatePeople/{number}")
	public ResponseEntity<?> generatePeople(@PathVariable("number") Integer numberOfPeople) {
		peopleService.generatePeople(numberOfPeople);
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}

}
