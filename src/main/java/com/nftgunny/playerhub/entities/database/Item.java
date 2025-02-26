package com.nftgunny.playerhub.entities.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nftgunny.core.entities.database.MongoDbEntity;
import com.nftgunny.playerhub.config.constant.ItemType;
import com.nftgunny.playerhub.entities.database.dto.AttackFigure;
import com.nftgunny.playerhub.entities.database.dto.DefenseFigure;
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

    @Field(name = "name")
    String name;

    @Field(name = "image_url")
    @JsonProperty("image_url")
    String imageUrl;

    @Field(name = "effect_url")
    @JsonProperty("effect_url")
    String effectUrl;

    @Field(name = "projectile_url")
    @JsonProperty("projectile_url")
    String projectileUrl;

    @Field(name = "default_price")
    @JsonProperty("default_price")
    Double defaultPrice;

    @Enumerated(EnumType.STRING)
    @Field(name = "type")
    ItemType type;

    @Field(name = "default_attack_figure")
    @JsonProperty("default_attack_figure")
    AttackFigure defaultAttackFigure;

    @Field(name = "default_defense_figure")
    @JsonProperty("default_defense_figure")
    DefenseFigure defaultDefenseFigure;
}
