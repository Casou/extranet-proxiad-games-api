package com.proxiad.games.extranet.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proxiad.games.extranet.enums.TextEnum;
import com.proxiad.games.extranet.model.Text;

@Repository
public interface TextRepository extends CrudRepository<Text, Integer> {

	List<Text> findAll();

	List<Text> findAllByOrderByIdAsc();

	List<Text> findAllByDiscriminantOrderByIdAsc(TextEnum discriminant);

}
