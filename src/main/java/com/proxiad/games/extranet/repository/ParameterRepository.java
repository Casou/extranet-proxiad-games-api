package com.proxiad.games.extranet.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proxiad.games.extranet.model.Parameter;

@Repository
public interface ParameterRepository extends CrudRepository<Parameter, Integer> {

	List<Parameter> findAll();

}
