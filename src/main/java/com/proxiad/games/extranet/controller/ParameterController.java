package com.proxiad.games.extranet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.model.Parameter;
import com.proxiad.games.extranet.repository.ParameterRepository;

@RestController
public class ParameterController {

	@Autowired
	private ParameterRepository parameterRepository;

	@GetMapping("/parametres")
	public List<Parameter> findAll() {
		return parameterRepository.findAll();
	}

}
