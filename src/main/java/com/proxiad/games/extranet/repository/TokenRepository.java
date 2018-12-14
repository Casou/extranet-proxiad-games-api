package com.proxiad.games.extranet.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proxiad.games.extranet.model.Token;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {
}
