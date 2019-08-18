package com.proxiad.games.extranet.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.proxiad.games.extranet.annotation.AdminTokenSecurity;
import com.proxiad.games.extranet.annotation.BypassSecurity;
import com.proxiad.games.extranet.exception.ProxiadControllerException;
import com.proxiad.games.extranet.model.Parameter;
import com.proxiad.games.extranet.repository.ParameterRepository;

@RestController
@CrossOrigin
public class ParameterController {

	@Autowired
	private ParameterRepository parameterRepository;

	@GetMapping("/parametre")
	@BypassSecurity
	public List<Parameter> findAll() {
		return parameterRepository.findAll();
	}

	@GetMapping("/parametre/{key}")
	@AdminTokenSecurity
	public Parameter findByKey(@PathVariable String key) throws ProxiadControllerException {
		Optional<Parameter> optParameter = parameterRepository.findByKey(key);
		return optParameter.orElseThrow(() -> new ProxiadControllerException("Parameter " + key + " not found"));
	}

	@PatchMapping("/parametre")
	@AdminTokenSecurity
	public Parameter updateParametre(@RequestBody Parameter changedParameter) throws ProxiadControllerException {
		Optional<Parameter> optParameter = parameterRepository.findById(changedParameter.getId());
		Parameter parameter = optParameter.orElseThrow(() -> new ProxiadControllerException("Parameter " + changedParameter.getId() + " not found"));

		parameter.setValue(changedParameter.getValue());
		parameterRepository.save(parameter);

		return parameter;
	}

}
