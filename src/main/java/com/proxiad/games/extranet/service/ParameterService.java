package com.proxiad.games.extranet.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proxiad.games.extranet.dto.ParameterDto;
import com.proxiad.games.extranet.enums.ParameterEnum;
import com.proxiad.games.extranet.exception.ProxiadControllerException;
import com.proxiad.games.extranet.model.Parameter;
import com.proxiad.games.extranet.repository.ParameterRepository;

@Service
public class ParameterService {

	@Autowired
	private ParameterRepository parameterRepository;

	private static ModelMapper mapper = new ModelMapper();

	public ParameterDto findByKey(String key) throws ProxiadControllerException {
		Optional<Parameter> optParameter = parameterRepository.findByKey(key);
		if (optParameter.isPresent()) {
			return mapper.map(optParameter.get(), ParameterDto.class);
		}

		return new ParameterDto(
				ParameterEnum.findByKey(key)
						.orElseThrow(() -> new ProxiadControllerException("Impossible to find parameter with key " + key))
		);
	}
}
