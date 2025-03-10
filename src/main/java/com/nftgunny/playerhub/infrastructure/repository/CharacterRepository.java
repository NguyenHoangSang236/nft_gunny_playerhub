package com.nftgunny.playerhub.infrastructure.repository;

import com.nftgunny.playerhub.entities.database.Character;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends MongoRepository<Character, String> {
    @Query("{ 'user_name': ?0 }")
    Optional<Character> findByUserName(String userName);
}
