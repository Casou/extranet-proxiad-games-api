package com.proxiad.games.extranet.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proxiad.games.extranet.model.People;

@Repository
public interface PeopleRepository extends CrudRepository<People, Integer> {
}
