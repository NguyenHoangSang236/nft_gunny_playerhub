package com.nftgunny.playerhub.infrastructure.repository;

import com.nftgunny.playerhub.config.constant.UserItemStatus;
import com.nftgunny.playerhub.entities.database.UserItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserItemRepository extends MongoRepository<UserItem, String> {
    @Query("{ 'user_name': ?0, 'item_information._id': ?1 }")
    Optional<UserItem> findByUserNameAndItemId(String userName, String itemId);

    @Query("{ 'user_name': ?0, '_id': ?1 }")
    Optional<UserItem> findByUserNameAndUserItemId(String userName, String userItemId);

    @Query("{ '_id': { '$in': ?0 }, 'user_name': ?1, 'status': ?2 }")
    List<UserItem> findByIdsAndUserNameAndStatus(List<String> ids, String userName, UserItemStatus status);

    @Query("{ '_id': { '$in': ?0 }, 'user_name': ?1}")
    List<UserItem> findByIdsAndUserName(List<String> ids, String userName);

    @Query("{ 'user_name': ?0, 'status': ?1 }")
    List<UserItem> findByUserNameAndStatus(String userName, UserItemStatus status);
}
