package com.proxiad.games.extranet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

	@PatchMapping
	@BypassSecurity
	public void updateVoice(@RequestBody Voice voiceUpdate) {
		Voice voice = voiceRepository.findByName(voiceUpdate.getName()).orElse(new Voice(voiceUpdate.getName()));

		voice.setPitch(voiceUpdate.getPitch());
		voice.setRate(voiceUpdate.getRate());
		voice.setVolume(voiceUpdate.getVolume());

		voiceRepository.save(voice);
	}

}
