package com.proxiad.games.extranet.controller;

import com.proxiad.games.extranet.annotation.AdminTokenSecurity;
import com.proxiad.games.extranet.annotation.BypassSecurity;
import com.proxiad.games.extranet.dto.ModifyTimeDto;
import com.proxiad.games.extranet.dto.RoomDto;
import com.proxiad.games.extranet.dto.RoomTrollDto;
import com.proxiad.games.extranet.mapper.RoomMapper;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.repository.RoomRepository;
import com.proxiad.games.extranet.service.RoomService;
import com.proxiad.games.extranet.service.TrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@CrossOrigin
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private TrollService trollService;

    @GetMapping("/room")
    @AdminTokenSecurity
    public List<RoomDto> listAllRooms() {
        return roomService.findAll();
    }

    @PutMapping(value = "/room")
    @AdminTokenSecurity
    public ResponseEntity<?> newRoom() {
        return new ResponseEntity<>(roomService.newRoom(), HttpStatus.OK);
    }

    @PostMapping(value = "/room/{id}/name")
    @AdminTokenSecurity
    public ResponseEntity<?> updateRoomName(@PathVariable("id") Integer id, @RequestBody RoomDto updatedRoom) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No room with id " + id));
        room.setName(updatedRoom.getName());
        roomRepository.save(room);

        return new ResponseEntity<>(updatedRoom.getName(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/room/{id}")
    @AdminTokenSecurity
    public ResponseEntity<?> deleteRoom(@PathVariable("id") Integer id) {
        roomService.deleteRoom(id);
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }

    @PatchMapping(value = "/room/{id}/reinit")
    @AdminTokenSecurity
    public ResponseEntity<?> reinitRoom(@PathVariable("id") Integer id) {
        Room room = roomService.reinitRoom(id);

        this.simpMessagingTemplate.convertAndSend("/topic/room/" + room.getId() + "/reinit", new RoomDto());
        return new ResponseEntity<>(roomMapper.toDto(room), HttpStatus.OK);
    }

    @RequestMapping("/user/troll")
    @BypassSecurity
    public void troll(@RequestParam("salle") String roomName) {
        Room room = roomRepository.findByNameIgnoreCase(roomName)
                .orElseThrow(() -> new EntityNotFoundException("Your room is unknown. Please contact the administrator."));

        final RoomTrollDto roomTrollDto = trollService.trollRoom(room);

        this.simpMessagingTemplate.convertAndSend("/topic/room/admin/troll", roomTrollDto);
        this.simpMessagingTemplate.convertAndSend("/topic/room/" + room.getId() + "/troll", roomTrollDto);
    }

    @PostMapping("/room/modifyTime")
    @AdminTokenSecurity
    public void modifyTime(@RequestBody ModifyTimeDto modifyTimeDto) {
        modifyTimeDto = trollService.modifyTime(modifyTimeDto);

        this.simpMessagingTemplate.convertAndSend("/topic/room/admin/modifyTime", modifyTimeDto);
        this.simpMessagingTemplate.convertAndSend("/topic/room/" + modifyTimeDto.getRoomId() + "/modifyTime", modifyTimeDto);
    }

}
