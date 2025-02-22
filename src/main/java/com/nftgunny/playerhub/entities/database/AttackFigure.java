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
    Double damage;

    @JsonProperty("critical_chance")
    Double criticalChance;

    @JsonProperty("life_steal_percentage")
    Double lifeStealPercentage;

    @JsonProperty("armor_penetration")
    Double armorPenetration;

    @JsonProperty("healing_reduce_percentage")
    Double healingReducePercentage;

    @JsonProperty("aiming_angle")
    Double aimingAngle;
}
