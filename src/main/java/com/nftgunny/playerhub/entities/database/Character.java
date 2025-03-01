package com.nftgunny.playerhub.entities.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nftgunny.core.entities.database.MongoDbEntity;
import com.nftgunny.playerhub.entities.database.dto.AttackFigure;
import com.nftgunny.playerhub.entities.database.dto.DefenseFigure;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Document("character")
@Builder
public class Character extends MongoDbEntity implements Serializable {
    @Getter
    @Setter
    @Id
    String id;

    @Getter
    @Setter
    @Field(name = "user_name")
    @JsonProperty("user_name")
    @Indexed(unique = true, name = "unique_user_name_index")
    String userName;

    @Getter
    @Setter
    @Field(name = "name")
    @Indexed(unique = true, name = "unique_character_name_index")
    String name;

    @Getter
    @Setter
    @Field(name = "image_url")
    @JsonProperty("image_url")
    String imageUrl;

    @Getter
    @Setter
    @Field(name = "level")
    Integer level;

    @Getter
    @Setter
    @Field(name = "combat_power")
    @JsonProperty("combat_power")
    Long combatPower;

    @Getter
    @Setter
    @Field(name = "attack_figure")
    @JsonProperty("attack_figure")
    AttackFigure attackFigure;

    @Getter
    @Setter
    @Field(name = "defense_figure")
    @JsonProperty("defense_figure")
    DefenseFigure defenseFigure;
}
