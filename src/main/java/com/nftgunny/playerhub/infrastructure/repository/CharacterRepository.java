package com.nftgunny.playerhub.infrastructure.repository;

import com.nftgunny.playerhub.entities.database.Character;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends MongoRepository<Character, Integer> {

}
