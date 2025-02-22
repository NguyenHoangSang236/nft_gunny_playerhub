package com.nftgunny.playerhub.entities.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nftgunny.core.entities.database.MongoDbEntity;
import com.nftgunny.playerhub.config.constant.ItemType;
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
@Document("item")
@Builder
public class Item extends MongoDbEntity implements Serializable {
    @Id
    String id;

    @Field(name = "user_id")
    @JsonProperty("user_id")
    String userId;

    @Field(name = "name")
    String name;

    @Field(name = "image_url")
    @JsonProperty("image_url")
    String imageUrl;

    @Field(name = "initial_price")
    @JsonProperty("initial_price")
    Double initialPrice;

    @Field(name = "current_price")
    @JsonProperty("current_price")
    Double currentPrice;

    @Field(name = "level")
    Integer level;

    @Field(name = "status")
    String status;

    @Field(name = "nft_hash_address")
    @JsonProperty("nft_hash_address")
    String nftHashAddress;

    @Enumerated(EnumType.STRING)
    @Field(name = "type")
    ItemType type;

    @Field(name = "attack_figure")
    @JsonProperty("attack_figure")
    AttackFigure attackFigure;

    @Field(name = "defense_figure")
    @JsonProperty("defense_figure")
    DefenseFigure defenseFigure;
}
