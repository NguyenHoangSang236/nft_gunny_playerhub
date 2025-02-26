package com.nftgunny.playerhub.infrastructure.repository;

import com.nftgunny.playerhub.entities.database.UserItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserItemRepository extends MongoRepository<UserItem, String> {
    @Query("{ 'user_name': ?0, 'item_information._id': ?1 }")
    Optional<UserItem> findByUserNameAndItemId(String userName, String itemId);
}
