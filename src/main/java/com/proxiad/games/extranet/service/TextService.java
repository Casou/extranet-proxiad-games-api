package com.proxiad.games.extranet.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proxiad.games.extranet.dto.TextDto;
import com.proxiad.games.extranet.exception.ProxiadControllerException;
import com.proxiad.games.extranet.model.Text;
import com.proxiad.games.extranet.repository.TextRepository;

@Service
public class TextService {

	@Autowired
	private TextRepository introSentenceRepository;

	public void updateSentence(TextDto introSentence) throws ProxiadControllerException {
		Optional<Text> optIntro = introSentenceRepository.findById(introSentence.getId());
		Text introSentenceToUpdate = optIntro.orElseThrow(() -> new ProxiadControllerException("Intro sentence not found for id " + introSentence.getId()));
		introSentenceToUpdate.setText(introSentence.getText());
		introSentenceToUpdate.setVoice(introSentence.getVoiceName());
		introSentenceRepository.save(introSentenceToUpdate);
	}

}
