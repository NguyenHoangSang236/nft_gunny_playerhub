package com.nftgunny.playerhub.entities.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nftgunny.core.entities.database.MongoDbEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("character")
@Builder
public class Character extends MongoDbEntity implements Serializable {

    @Id
    String id;


    @Field(name = "user_id")
    @JsonProperty("user_id")
    @Indexed(unique = true, name = "unique_user_id_index")
    String userId;


    @Field(name = "name")
    @Indexed(unique = true, name = "unique_character_name_index")
    String name;


    @Field(name = "image_url")
    @JsonProperty("image_url")
    String imageUrl;


    @Field(name = "level")
    Integer level;


    @Field(name = "combat_power")
    @JsonProperty("combat_power")
    Long combatPower;


    @Field(name = "equipped_item_ids")
    @JsonProperty("equipped_item_ids")
    List<String> equippedItemIds;


    @Field(name = "attack_figure")
    @JsonProperty("attack_figure")
    AttackFigure attackFigure;


    @Field(name = "defense_figure")
    @JsonProperty("defense_figure")
    DefenseFigure defenseFigure;
}
