package com.proxiad.games.extranet.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="player_profile")
@NoArgsConstructor
public class PlayerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name="room_id", nullable=false)
    @JsonIgnore
    private Room room;

}
