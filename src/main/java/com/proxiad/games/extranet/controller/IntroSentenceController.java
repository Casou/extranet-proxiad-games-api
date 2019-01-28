package com.proxiad.games.extranet.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.proxiad.games.extranet.annotation.AdminTokenSecurity;
import com.proxiad.games.extranet.exception.ProxiadControllerException;
import com.proxiad.games.extranet.model.IntroSentence;
import com.proxiad.games.extranet.repository.IntroSentenceRepository;
import com.proxiad.games.extranet.service.IntroSentenceService;

@RestController
@CrossOrigin
public class IntroSentenceController {

	@Autowired
	private IntroSentenceRepository introSentenceRepository;

	@Autowired
	private IntroSentenceService introSentenceService;

	@GetMapping("/intro_sentences")
	@AdminTokenSecurity
	public List<IntroSentence> findAll() {
		return introSentenceRepository.findAllByOrderByIdAsc();
	}

	@PostMapping("/intro_sentence")
	@AdminTokenSecurity
	public IntroSentence createSentence() {
		return introSentenceRepository.save(new IntroSentence());
	}

	@PatchMapping("/intro_sentence")
	@AdminTokenSecurity
	public void updateSentence(@RequestBody IntroSentence introSentence) throws ProxiadControllerException {
		introSentenceService.updateSentence(introSentence);
	}

	@DeleteMapping("/intro_sentence")
	@AdminTokenSecurity
	public void deleteSentence(@RequestBody IntroSentence introSentence) throws ProxiadControllerException {
		Optional<IntroSentence> optIntro = introSentenceRepository.findById(introSentence.getId());
		IntroSentence introSentenceToDelete = optIntro.orElseThrow(() -> new ProxiadControllerException("Intro sentence not found for id " + introSentence.getId()));
		introSentenceRepository.delete(introSentenceToDelete);
	}

}
