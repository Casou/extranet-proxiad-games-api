package com.proxiad.games.extranet.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proxiad.games.extranet.exception.ProxiadControllerException;
import com.proxiad.games.extranet.model.IntroSentence;
import com.proxiad.games.extranet.repository.IntroSentenceRepository;

@Service
public class IntroSentenceService {

	@Autowired
	private IntroSentenceRepository introSentenceRepository;

	public void updateSentence(IntroSentence introSentence) throws ProxiadControllerException {
		Optional<IntroSentence> optIntro = introSentenceRepository.findById(introSentence.getId());
		IntroSentence introSentenceToUpdate = optIntro.orElseThrow(() -> new ProxiadControllerException("Intro sentence not found for id " + introSentence.getId()));
		introSentenceToUpdate.setText(introSentence.getText());
		introSentenceToUpdate.setVoice(introSentence.getVoice());
		introSentenceRepository.save(introSentenceToUpdate);
	}

}
