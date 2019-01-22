package com.proxiad.games.extranet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proxiad.games.extranet.model.Parameter;

@Repository
public interface ParameterRepository extends CrudRepository<Parameter, Integer> {

	List<Parameter> findAll();

	Optional<Parameter> findByKey(String key);

}
