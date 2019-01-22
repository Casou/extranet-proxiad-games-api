package com.proxiad.games.extranet.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.proxiad.games.extranet.annotation.AdminTokenSecurity;
import com.proxiad.games.extranet.annotation.BypassSecurity;
import com.proxiad.games.extranet.dto.ParameterDto;
import com.proxiad.games.extranet.exception.ProxiadControllerException;
import com.proxiad.games.extranet.model.Parameter;
import com.proxiad.games.extranet.repository.ParameterRepository;
import com.proxiad.games.extranet.service.ParameterService;

@RestController
public class ParameterController {

	@Autowired
	private ParameterRepository parameterRepository;

	@Autowired
	private ParameterService parameterService;

	@GetMapping("/parametres")
	@BypassSecurity
	public List<Parameter> findAll() {
		return parameterRepository.findAll();
	}

	@PostMapping("/parametre")
	@AdminTokenSecurity
	public ParameterDto findByKey(@RequestBody ParameterDto input) throws ProxiadControllerException {
		return parameterService.findByKey(input.getKey());
	}

	@PostMapping("/parametres")
	@AdminTokenSecurity
	public void updateParametre(@RequestBody Parameter changedParameter) throws ProxiadControllerException {
		Optional<Parameter> optParameter = parameterRepository.findById(changedParameter.getId());
		Parameter parameter = optParameter.orElseThrow(() -> new ProxiadControllerException("Parameter " + changedParameter.getId() + " not found"));

		parameter.setValue(changedParameter.getValue());
		parameterRepository.save(parameter);
	}

}
