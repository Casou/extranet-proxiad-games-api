package com.proxiad.games.extranet.controller;

import com.proxiad.games.extranet.annotation.AdminTokenSecurity;
import com.proxiad.games.extranet.dto.PlayerProfileDto;
import com.proxiad.games.extranet.model.PlayerProfile;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.repository.PlayerProfileRepository;
import com.proxiad.games.extranet.repository.RoomRepository;
import com.proxiad.games.extranet.service.PlayerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@CrossOrigin
public class PlayerProfileController {

    @Autowired
    private PlayerProfileService playerProfileService;

    @Autowired
    private PlayerProfileRepository playerProfileRepository;

    @Autowired
    private RoomRepository roomRepository;

    @GetMapping("/player-profile/{roomId}")
    @AdminTokenSecurity
    public List<PlayerProfile> getAllProfilesForRoom(@PathVariable("roomId") Integer roomId) {
        return playerProfileRepository.findByRoomId(roomId);
    }

    @PutMapping("/player-profile")
    @AdminTokenSecurity
    public PlayerProfile createNewProfile(@RequestBody PlayerProfileDto playerProfileDto) {
        return playerProfileService.createNewProfile(playerProfileDto.getRoomId());
    }

    @PatchMapping("/player-profile/{playerProfileId}")
    @AdminTokenSecurity
    public PlayerProfile updatePlayerProfile(@PathVariable("playerProfileId") Integer playerProfileId,
                                             @RequestBody PlayerProfileDto playerProfileDto) {
        return playerProfileService.updateProfile(playerProfileId, playerProfileDto);
    }

    @DeleteMapping("/player-profile/{playerProfileId}")
    @AdminTokenSecurity
    public void deletePlayerProfile(@PathVariable("playerProfileId") Integer playerProfileId) {
        PlayerProfile profile = playerProfileRepository.findById(playerProfileId)
                .orElseThrow(() -> new EntityNotFoundException("Player profile not found for id " + playerProfileId));

        Room room = roomRepository.findById(profile.getRoom().getId())
                .orElseThrow(() -> new EntityNotFoundException("No riddle's room with id " + profile.getRoom().getId()));

        room.getPlayerProfiles().remove(profile);

        playerProfileRepository.delete(profile);
        roomRepository.save(room);
    }

}
