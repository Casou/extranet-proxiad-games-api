package com.proxiad.games.extranet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.annotation.BypassSecurity;
import com.proxiad.games.extranet.model.Voice;
import com.proxiad.games.extranet.repository.VoiceRepository;

@RestController
@RequestMapping("/voice")
@CrossOrigin
public class VoiceController {

	@Autowired
	private VoiceRepository voiceRepository;

	@GetMapping
	@BypassSecurity
	public List<Voice> findAll() {
		return voiceRepository.findAll();
	}

}
