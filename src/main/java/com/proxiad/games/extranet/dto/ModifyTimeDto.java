package com.proxiad.games.extranet.dto;

import com.proxiad.games.extranet.model.Voice;
import lombok.Data;

@Data
public class ModifyTimeDto {

    private Integer roomId;
    private Integer time;
    private String message;
    private Voice voice;
    private String startTime;
    private Integer remainingTime;

}
