package com.proxiad.games.extranet.repository;

import com.proxiad.games.extranet.model.PlayerProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerProfileRepository extends CrudRepository<PlayerProfile, Integer> {

	@Query("select p from PlayerProfile p where p.room.id = :roomId")
	List<PlayerProfile> findByRoomId(@Param("roomId") Integer roomId);

}
