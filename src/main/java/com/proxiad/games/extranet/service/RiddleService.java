package com.proxiad.games.extranet.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proxiad.games.extranet.dto.RiddleDto;
import com.proxiad.games.extranet.dto.UnlockDto;
import com.proxiad.games.extranet.enums.RiddleType;
import com.proxiad.games.extranet.exception.PasswordDontMatchException;
import com.proxiad.games.extranet.exception.RiddleAlreadySolvedException;
import com.proxiad.games.extranet.model.Riddle;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.repository.RiddleRepository;
import com.proxiad.games.extranet.repository.RoomRepository;

@Service
public class RiddleService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RiddleRepository riddleRepository;

    public Riddle newRiddle(Integer roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("No room with id " + roomId));

        return riddleRepository.save(Riddle.builder()
                .name("Nouveau")
                .riddleId("idToSet")
                .room(room)
                .resolved(false)
                .type(RiddleType.GAME)
                .build());
    }

    public Riddle resolveOpenDoorRiddle(RiddleDto riddleDto) {
        Room room = roomRepository.findById(riddleDto.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("No room with id " + riddleDto.getRoomId()));
        Riddle openDoorRiddle = room.getRiddles().stream()
                .filter(riddle -> riddle.getType().equals(RiddleType.OPEN_DOOR))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No open room riddle for room with id " + riddleDto.getRoomId()));

        if (openDoorRiddle.getRiddlePassword().equals(riddleDto.getRiddlePassword())) {
            openDoorRiddle.setResolved(true);
            riddleRepository.save(openDoorRiddle);
        }
        return openDoorRiddle;
    }


    public void resolveRiddle(UnlockDto unlockDto, Room room) throws PasswordDontMatchException, RiddleAlreadySolvedException {
        Riddle riddle = room.getRiddles().stream()
                .filter(r -> r.getId().equals(unlockDto.getId()))
                .findFirst()
                .orElse(null);
        if (riddle == null) {
            throw new EntityNotFoundException();
        }

        if (riddle.getResolved()) {
            throw new RiddleAlreadySolvedException();
        }

        if (!riddle.getRiddlePassword().equals(unlockDto.getPassword())) {
            throw new PasswordDontMatchException();
        }

        riddle.setResolved(true);
        riddleRepository.save(riddle);
    }

}
