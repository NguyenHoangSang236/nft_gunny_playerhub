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
public class AttackFigure extends Figure implements Serializable {
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


    public AttackFigure add(AttackFigure atk) {
        this.damage = Objects.requireNonNullElse(this.damage, 0) + Objects.requireNonNullElse(atk.getDamage(), 0);
        this.criticalChance = Objects.requireNonNullElse(this.criticalChance, 0.0) + Objects.requireNonNullElse(atk.getCriticalChance(), 0.0);
        this.lifeStealPercentage = Objects.requireNonNullElse(this.lifeStealPercentage, 0) + Objects.requireNonNullElse(atk.getLifeStealPercentage(), 0);
        this.armorPenetration = Objects.requireNonNullElse(this.armorPenetration, 0) + Objects.requireNonNullElse(atk.getArmorPenetration(), 0);
        this.healingReducePercentage = Objects.requireNonNullElse(this.healingReducePercentage, 0) + Objects.requireNonNullElse(atk.getHealingReducePercentage(), 0);

        return this;
    }

    public AttackFigure subtract(AttackFigure atk) {
        this.damage = Objects.requireNonNullElse(this.damage, 0) - Objects.requireNonNullElse(atk.getDamage(), 0);
        this.criticalChance = Objects.requireNonNullElse(this.criticalChance, 0.0) - Objects.requireNonNullElse(atk.getCriticalChance(), 0.0);
        this.lifeStealPercentage = Objects.requireNonNullElse(this.lifeStealPercentage, 0) - Objects.requireNonNullElse(atk.getLifeStealPercentage(), 0);
        this.armorPenetration = Objects.requireNonNullElse(this.armorPenetration, 0) - Objects.requireNonNullElse(atk.getArmorPenetration(), 0);
        this.healingReducePercentage = Objects.requireNonNullElse(this.healingReducePercentage, 0) - Objects.requireNonNullElse(atk.getHealingReducePercentage(), 0);

        return this;
    }

}
