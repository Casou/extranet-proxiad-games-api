package com.proxiad.games.extranet.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.proxiad.games.extranet.annotation.AdminTokenSecurity;
import com.proxiad.games.extranet.dto.TextDto;
import com.proxiad.games.extranet.enums.TextEnum;
import com.proxiad.games.extranet.exception.ProxiadControllerException;
import com.proxiad.games.extranet.model.Text;
import com.proxiad.games.extranet.repository.TextRepository;
import com.proxiad.games.extranet.service.TextService;

@RestController
@RequestMapping("/text")
@CrossOrigin
public class TextController {

	@Autowired
	private TextRepository textRepository;

	@Autowired
	private TextService textService;

	@GetMapping("/intro")
	@AdminTokenSecurity
	public List<Text> findAll() {
		return textRepository.findAllByDiscriminantOrderByIdAsc(TextEnum.INTRO);
	}

	@PostMapping("/intro")
	@AdminTokenSecurity
	public Text createSentence() {
		return textRepository.save(new Text(TextEnum.INTRO));
	}

	@PatchMapping("/intro")
	@AdminTokenSecurity
	public void updateSentence(@RequestBody TextDto introSentence) throws ProxiadControllerException {
		textService.updateSentence(introSentence);
	}

	@DeleteMapping("/intro")
	@AdminTokenSecurity
	public void deleteSentence(@RequestBody Text introSentence) throws ProxiadControllerException {
		Optional<Text> optIntro = textRepository.findById(introSentence.getId());
		Text introSentenceToDelete = optIntro.orElseThrow(() -> new ProxiadControllerException("Text not found for id " + introSentence.getId()));
		textRepository.delete(introSentenceToDelete);
	}

}
