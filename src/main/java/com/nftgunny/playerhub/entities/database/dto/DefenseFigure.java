package com.nftgunny.playerhub.entities.database.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nftgunny.core.entities.database.Figure;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefenseFigure extends Figure implements Serializable {
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

    public DefenseFigure add(DefenseFigure def) {
        this.healthPoint = Objects.requireNonNullElse(this.healthPoint, 0) + Objects.requireNonNullElse(def.getHealthPoint(), 0);
        this.manaPoint = Objects.requireNonNullElse(this.manaPoint, 0) + Objects.requireNonNullElse(def.getManaPoint(), 0);
        this.cooldownReducePercentage = Objects.requireNonNullElse(this.cooldownReducePercentage, 0) + Objects.requireNonNullElse(def.getCooldownReducePercentage(), 0);
        this.armor = Objects.requireNonNullElse(this.armor, 0) + Objects.requireNonNullElse(def.getArmor(), 0);
        this.dodgeChance = Objects.requireNonNullElse(this.dodgeChance, 0.0) + Objects.requireNonNullElse(def.getDodgeChance(), 0.0);
        this.healingSpeed = Objects.requireNonNullElse(this.healingSpeed, 0) + Objects.requireNonNullElse(def.getHealingSpeed(), 0);

        return this;
    }

    public DefenseFigure subtract(DefenseFigure def) {
        this.healthPoint = Math.max(0, Objects.requireNonNullElse(this.healthPoint, 0) - Objects.requireNonNullElse(def.getHealthPoint(), 0));
        this.manaPoint = Math.max(0, Objects.requireNonNullElse(this.manaPoint, 0) - Objects.requireNonNullElse(def.getManaPoint(), 0));
        this.cooldownReducePercentage = Math.max(0, Objects.requireNonNullElse(this.cooldownReducePercentage, 0) - Objects.requireNonNullElse(def.getCooldownReducePercentage(), 0));
        this.armor = Math.max(0, Objects.requireNonNullElse(this.armor, 0) - Objects.requireNonNullElse(def.getArmor(), 0));
        this.dodgeChance = Objects.requireNonNullElse(this.dodgeChance, 0.0) - Objects.requireNonNullElse(def.getDodgeChance(), 0.0);
        this.healingSpeed = Math.max(0, Objects.requireNonNullElse(this.healingSpeed, 0) - Objects.requireNonNullElse(def.getHealingSpeed(), 0));

        return this;
    }

}
