package com.proxiad.games.extranet.repository;

import com.proxiad.games.extranet.model.Riddle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiddleRepository extends CrudRepository<Riddle, Integer> {

	List<Riddle> findAll();

}
