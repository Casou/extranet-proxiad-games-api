package com.proxiad.games.extranet.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proxiad.games.extranet.dto.TextDto;
import com.proxiad.games.extranet.enums.TextEnum;
import com.proxiad.games.extranet.exception.ProxiadControllerException;
import com.proxiad.games.extranet.model.Riddle;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.model.Text;
import com.proxiad.games.extranet.model.Voice;
import com.proxiad.games.extranet.repository.TextRepository;
import com.proxiad.games.extranet.repository.VoiceRepository;

@Service
public class TextService {

	@Autowired
	private TextRepository introSentenceRepository;

	@Autowired
	private VoiceRepository voiceRepository;

	@Autowired
	private TextRepository textRepository;

	public void updateSentence(TextDto introSentence) throws ProxiadControllerException {
		Optional<Text> optIntro = introSentenceRepository.findById(introSentence.getId());
		Text introSentenceToUpdate = optIntro.orElseThrow(() -> new ProxiadControllerException("Intro sentence not found for id " + introSentence.getId()));
		introSentenceToUpdate.setText(introSentence.getText());
		introSentenceToUpdate.setVoiceName(introSentence.getVoiceName());
		introSentenceRepository.save(introSentenceToUpdate);
	}

	public TextDto mapToDto(Text text) {
		TextDto textDto = new TextDto(text);
		textDto.setVoice(voiceRepository.findByName(text.getVoiceName()).orElse(new Voice()));
		return textDto;
	}

	public Text getTextToSendForRiddleResolution(Room room) {
		final Text textToSend;
		List<Riddle> resolvedRiddles = room.getRiddles().stream().filter(Riddle::getResolved).collect(Collectors.toList());
		if (resolvedRiddles.size() == room.getRiddles().size()) {
			return textRepository.findAllByDiscriminantOrderByIdAsc(TextEnum.LAST_ENIGMA).get(0);
		}

		return textRepository.findAllByDiscriminantOrderByIdAsc(TextEnum.ENIGMA).get(resolvedRiddles.size());
	}

}
