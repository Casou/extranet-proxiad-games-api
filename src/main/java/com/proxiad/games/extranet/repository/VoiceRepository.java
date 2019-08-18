package com.proxiad.games.extranet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proxiad.games.extranet.model.Voice;

@Repository
public interface VoiceRepository extends CrudRepository<Voice, Integer> {

	List<Voice> findAll();

	Optional<Voice> findByName(String name);

}
