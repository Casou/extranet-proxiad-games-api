package com.proxiad.games.extranet.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proxiad.games.extranet.model.UnlockedRiddle;

@Repository
public interface UnlockedRiddleRepository extends CrudRepository<UnlockedRiddle, Integer> {

	List<UnlockedRiddle> findByTokenUser(String tokenUser);

}
