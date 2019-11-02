package com.proxiad.games.extranet.service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proxiad.games.extranet.dto.RoomTrollDto;
import com.proxiad.games.extranet.enums.MandatoryParameter;
import com.proxiad.games.extranet.enums.TextEnum;
import com.proxiad.games.extranet.model.*;
import com.proxiad.games.extranet.repository.ParameterRepository;
import com.proxiad.games.extranet.repository.RoomRepository;
import com.proxiad.games.extranet.repository.TextRepository;
import com.proxiad.games.extranet.repository.VoiceRepository;

@Service
public class TrollService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private TextRepository textRepository;

    @Autowired
    private VoiceRepository voiceRepository;

    public RoomTrollDto trollRoom(Room room) {
        Optional<Parameter> decreaseTimeParameter = parameterRepository.findByKey(MandatoryParameter.TROLL_DECREASE_TIME.getKey());
        Integer decreaseTime = Integer.parseInt(decreaseTimeParameter.orElse(
                Parameter.builder()
                        .key(MandatoryParameter.TROLL_DECREASE_TIME.getKey())
                        .value(MandatoryParameter.TROLL_DECREASE_TIME.getDefaultValue())
                        .type(MandatoryParameter.TROLL_DECREASE_TIME.getType()
                        )
                        .build())
                .getValue());

        final List<Text> trollTexts = textRepository.findAllByDiscriminantOrderByIdAsc(TextEnum.TROLL);

        final Timer timer = Optional.ofNullable(room.getTimer())
                .orElseThrow(() -> new EntityNotFoundException("No timer found for the room " + room.getName()));
        timer.setRemainingTime(Math.max(0, timer.getRemainingTime() - decreaseTime));
        room.setTimer(timer);
        final Integer trollIndex = room.getTrollIndex();
        final Integer newTrollIndex = trollIndex + 1 >= trollTexts.size() ? trollTexts.size() - 1 : trollIndex + 1;
        room.setTrollIndex(newTrollIndex);
        roomRepository.save(room);

        final Text trollText = trollTexts.get(trollIndex);
        final Voice voice = voiceRepository.findByName(trollText.getVoiceName()).orElse(new Voice());
        return RoomTrollDto.builder()
                .id(room.getId())
                .name(room.getName())
                .reduceTime(decreaseTime)
                .message(trollText.getText())
                .voice(voice)
                .videoName(trollText.getVideoName())
                .build();
    }

}
