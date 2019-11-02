package com.proxiad.games.extranet.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proxiad.games.extranet.dto.PlayerProfileDto;
import com.proxiad.games.extranet.model.PlayerProfile;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.repository.PlayerProfileRepository;
import com.proxiad.games.extranet.repository.RoomRepository;

@Service
public class PlayerProfileService {

    @Autowired
    private PlayerProfileRepository playerProfileRepository;

    @Autowired
    private RoomRepository roomRepository;

    public PlayerProfile createNewProfile(Integer roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found for id " + roomId));

        PlayerProfile profile = new PlayerProfile();
        profile.setName("*N*ouveau");
        profile.setRoom(room);

        return playerProfileRepository.save(profile);
    }

    public PlayerProfile updateProfile(Integer playerProfileId, PlayerProfileDto playerProfileDto) {
        PlayerProfile profile = playerProfileRepository.findById(playerProfileId)
                .orElseThrow(() -> new EntityNotFoundException("Player profile not found for id " + playerProfileId));

        profile.setName(playerProfileDto.getName());

        return playerProfileRepository.save(profile);
    }

}
