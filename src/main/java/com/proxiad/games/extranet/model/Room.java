package com.proxiad.games.extranet.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "room")
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String name;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "token_id")
    @ToString.Exclude
    private Token connectedToken;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "room", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Riddle> riddles = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "room")
    @ToString.Exclude
    private List<PlayerProfile> playerProfiles = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "timer_id")
    private Timer timer;

    private Boolean isTerminated = false;
    private String terminateStatus;

    @NotNull
    private Integer trollIndex = 0;

    @NotNull
    private Double audioBackgroundVolume = 0.0;

}
