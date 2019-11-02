package com.proxiad.games.extranet.repository;

import com.proxiad.games.extranet.model.Timer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimerRepository extends CrudRepository<Timer, Integer> {

}
