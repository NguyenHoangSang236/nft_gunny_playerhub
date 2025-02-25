package com.nftgunny.playerhub.entities.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttackFigure implements Serializable {
    Integer damage;

    @JsonProperty("critical_chance")
    Double criticalChance;

    @JsonProperty("life_steal_percentage")
    Integer lifeStealPercentage;

    @JsonProperty("armor_penetration")
    Integer armorPenetration;

    @JsonProperty("healing_reduce_percentage")
    Integer healingReducePercentage;

    @JsonProperty("aiming_angle")
    Integer aimingAngle;
}
