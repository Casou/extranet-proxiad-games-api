package com.proxiad.games.extranet;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.proxiad.games.extranet.enums.ParameterEnum;
import com.proxiad.games.extranet.model.Parameter;
import com.proxiad.games.extranet.repository.ParameterRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StartupCheckRunner implements CommandLineRunner {

	@Autowired
	private ParameterRepository repository;

	@Override
	public void run(String... args) {
		List<String> unexistingParameters = repository.findAll().stream()
				.map(Parameter::getKey)
				.filter(parameterKey -> !ParameterEnum.findByKey(parameterKey).isPresent())
				.collect(Collectors.toList());

		if (!unexistingParameters.isEmpty()) {
			log.error("There are unexisting parameters in database : " + String.join(", ", unexistingParameters));
		}

	}

}
