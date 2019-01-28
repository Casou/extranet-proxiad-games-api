package com.proxiad.games.extranet.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proxiad.games.extranet.model.IntroSentence;

@Repository
public interface IntroSentenceRepository extends CrudRepository<IntroSentence, Integer> {

	List<IntroSentence> findAll();

}
