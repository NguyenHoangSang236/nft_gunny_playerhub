package com.nftgunny.playerhub.infrastructure.repository;

import com.nftgunny.playerhub.entities.database.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, Integer> {
    @Query("{'user_name': ?0, 'password': ?1}")
    Optional<User> getAccountByUserNameAndPassword(String userName, String password);

    @Query("{'user_name': ?0}")
    Optional<User> getAccountByUserName(String userName);
    //helloworld
}
