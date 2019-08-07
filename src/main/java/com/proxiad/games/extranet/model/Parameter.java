package com.proxiad.games.extranet.model;

import javax.persistence.*;

import org.springframework.lang.NonNull;

import com.proxiad.games.extranet.enums.ParameterType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "parameter")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parameter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NonNull
    private String key;

    private String description;

    @NonNull
    private String value;

    @Enumerated(value = EnumType.STRING)
    @NonNull
    private ParameterType type;

    private String optionals;

}
