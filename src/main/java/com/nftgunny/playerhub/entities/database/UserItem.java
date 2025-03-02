package com.nftgunny.playerhub.entities.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nftgunny.core.entities.database.MongoDbEntity;
import com.nftgunny.playerhub.config.constant.UserItemStatus;
import com.nftgunny.playerhub.entities.database.dto.AttackFigure;
import com.nftgunny.playerhub.entities.database.dto.DefenseFigure;
import com.nftgunny.playerhub.entities.database.dto.UserItemInfoDto;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("user_item")
@Builder
public class UserItem extends MongoDbEntity implements Serializable {
    @Id
    String id;

    @Field(name = "user_name")
    @JsonProperty("user_name")
    String userName;

    @Field(name = "initial_price")
    @JsonProperty("initial_price")
    Double initialPrice;

    @Field(name = "current_price")
    @JsonProperty("current_price")
    Double currentPrice;

    @Field(name = "level")
    Integer level;

    @Field(name = "nft_hash_address")
    @JsonProperty("nft_hash_address")
    String nftHashAddress;

    @Field(name = "attack_figure")
    @JsonProperty("attack_figure")
    AttackFigure attackFigure;

    @Field(name = "defense_figure")
    @JsonProperty("defense_figure")
    DefenseFigure defenseFigure;

    @Field(name = "item_information")
    @JsonProperty("item_information")
    UserItemInfoDto itemInfo;

    @Enumerated(EnumType.STRING)
    @Field(name = "status")
    @JsonProperty("status")
    UserItemStatus status;
}
