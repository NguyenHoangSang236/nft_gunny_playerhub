package com.nftgunny.playerhub.entities.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefenseFigure implements Serializable {
    @JsonProperty("health_point")
    Integer healthPoint;

    @JsonProperty("mana_point")
    Integer manaPoint;

    @JsonProperty("cooldown_reduce_percentage")
    Integer cooldownReducePercentage;

    Integer armor;

    @JsonProperty("dodge_chance")
    Double dodgeChance;

    @JsonProperty("healing_speed")
    Integer healingSpeed;
}
