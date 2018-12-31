package com.proxiad.games.extranet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proxiad.games.extranet.model.Room;

@Repository
public interface RoomRepository extends CrudRepository<Room, Integer> {

	List<Room> findAll();

	Optional<Room> findByName(String name);

	@Query("Select r from Room r where r.connectedToken.token = :token")
	Optional<Room> findByToken(@Param("token") String token);

}
