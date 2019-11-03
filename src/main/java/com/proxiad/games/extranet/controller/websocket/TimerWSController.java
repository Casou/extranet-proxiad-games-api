package com.proxiad.games.extranet.controller.websocket;

import com.proxiad.games.extranet.dto.RoomDto;
import com.proxiad.games.extranet.dto.RoomTrollDto;
import com.proxiad.games.extranet.enums.MandatoryParameter;
import com.proxiad.games.extranet.enums.TimerStatusEnum;
import com.proxiad.games.extranet.exception.ProxiadControllerException;
import com.proxiad.games.extranet.mapper.RoomMapper;
import com.proxiad.games.extranet.model.Parameter;
import com.proxiad.games.extranet.model.Room;
import com.proxiad.games.extranet.model.Timer;
import com.proxiad.games.extranet.repository.ParameterRepository;
import com.proxiad.games.extranet.repository.RoomRepository;
import com.proxiad.games.extranet.repository.TimerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@Transactional
public class TimerWSController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private TimerRepository timerRepository;


    @MessageMapping("/room/start")
    public void start(RoomDto roomDto) throws ProxiadControllerException {
        final Room room = getRoom(roomDto);

        Optional<Parameter> defaultVolumeParameter = parameterRepository.findByKey(MandatoryParameter.AUDIO_BACKGROUND_DEFAULT_VOLUME.getKey());
        Double defaultAudioVolume = Double.valueOf(
                defaultVolumeParameter.orElse(Parameter.builder()
                        .key(MandatoryParameter.AUDIO_BACKGROUND_DEFAULT_VOLUME.getKey())
                        .value(MandatoryParameter.AUDIO_BACKGROUND_DEFAULT_VOLUME.getDefaultValue())
                        .type(MandatoryParameter.AUDIO_BACKGROUND_DEFAULT_VOLUME.getType())
                        .build())
                        .getValue());

        Timer timer = Optional.ofNullable(room.getTimer()).orElse(new Timer());
        timer.setStatus(TimerStatusEnum.INITIALIZING);
        timer.setRemainingTime(Math.max(0, roomDto.getRemainingTime()));
        timer = timerRepository.save(timer);
        room.setTimer(timer);
        room.setAudioBackgroundVolume(defaultAudioVolume);
        roomRepository.save(room);

        roomDto.setAudioBackgroundVolume(defaultAudioVolume);
        this.simpMessagingTemplate.convertAndSend("/topic/room/admin/start", roomDto);
        this.simpMessagingTemplate.convertAndSend("/topic/room/" + roomDto.getId() + "/start", roomDto);
    }

    @MessageMapping("/room/startTimer")
    public void startTimer(RoomDto roomDto) throws ProxiadControllerException {
        final Room room = getRoom(roomDto);

        final Timer timer = Optional.ofNullable(room.getTimer()).orElseThrow(() -> new ProxiadControllerException("No timer found for the room " + room.getName()));
        timer.setServerStartTime(LocalDateTime.now());
        timer.setClientStartTime(roomDto.getStartTime());
        timer.setStatus(TimerStatusEnum.STARTED);
        room.setTimer(timer);
        roomRepository.save(room);

        this.simpMessagingTemplate.convertAndSend("/topic/room/admin/startTimer", roomMapper.toDto(room));
    }

    @MessageMapping("/room/reduceTime")
    @SendTo("/topic/room/admin/reduceTime")
    public RoomTrollDto reduceTimerByTroll(RoomTrollDto roomTrollDto) throws ProxiadControllerException {
        Optional<Room> optRoom = roomRepository.findById(roomTrollDto.getId());
        if (!optRoom.isPresent()) {
            throw new ProxiadControllerException("Room with id " + roomTrollDto.getId() + " not found");
        }

        Timer timer = optRoom.get().getTimer();
        roomTrollDto.setStartTime(timer.getClientStartTime());
        roomTrollDto.setRemainingTime(timer.getRemainingTime());

        return roomTrollDto;
    }

//    @MessageMapping("/room/reduceTime")
//    @SendTo("/topic/room/admin/reduceTime")
//    public RoomTrollDto reduceTimerByTroll(ModifyTimeDto modifyTimeDto) throws ProxiadControllerException {
//        Optional<Room> optRoom = roomRepository.findById(roomTrollDto.getId());
//        if (!optRoom.isPresent()) {
//            throw new ProxiadControllerException("Room with id " + roomTrollDto.getId() + " not found");
//        }
//
//        Timer timer = optRoom.get().getTimer();
//        roomTrollDto.setStartTime(timer.getClientStartTime());
//        roomTrollDto.setRemainingTime(timer.getRemainingTime());
//
//        return roomTrollDto;
//    }

    @MessageMapping("/room/pause")
    public void pause(RoomDto roomDto) throws ProxiadControllerException {
        final Room room = getRoom(roomDto);

        final Timer timer = Optional.ofNullable(room.getTimer()).orElseThrow(() -> new ProxiadControllerException("No timer found for the room " + room.getName()));

        timer.setServerStartTime(LocalDateTime.now());
        timer.setClientStartTime(roomDto.getStartTime());
        timer.setStatus(TimerStatusEnum.PAUSED);
        timer.setRemainingTime(timer.calculatedRemainingTime());

        room.setTimer(timer);
        roomRepository.save(room);

        RoomDto payload = roomMapper.toDto(room);
        this.simpMessagingTemplate.convertAndSend("/topic/room/admin/pause", payload);
        this.simpMessagingTemplate.convertAndSend("/topic/room/" + roomDto.getId() + "/pause", payload);
    }

    private Room getRoom(RoomDto roomDto) throws ProxiadControllerException {
        return roomRepository.findById(roomDto.getId())
                .orElseThrow(() -> new ProxiadControllerException("Room " + roomDto.getId() + " doesn't exists"));
    }

}
