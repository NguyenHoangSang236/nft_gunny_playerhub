package com.nftgunny.playerhub.services;

import com.nftgunny.core.config.constant.ConstantValue;
import com.nftgunny.core.config.constant.SystemConfigCriteria;
import com.nftgunny.core.repository.SystemConfigRepository;
import com.nftgunny.playerhub.entities.database.Character;
import com.nftgunny.playerhub.entities.database.UserItem;
import com.nftgunny.playerhub.entities.database.dto.AttackFigure;
import com.nftgunny.playerhub.entities.database.dto.DefenseFigure;
import com.nftgunny.playerhub.infrastructure.repository.CharacterRepository;
import org.springframework.stereotype.Service;

@Service
public class FigureCalculationServiceImpl implements FigureCalculationService{
    final SystemConfigRepository systemConfigRepo;
    final CharacterRepository characterRepo;

    public FigureCalculationServiceImpl(SystemConfigRepository systemConfigRepo, CharacterRepository characterRepo) {
        this.systemConfigRepo = systemConfigRepo;
        this.characterRepo = characterRepo;
    }


    @Override
    public Character calculateCharacterFigureByUserNamAndUserItemId(String userName, String newEquippedItemId, String removedItemId) {
        return null;
    }

    @Override
    public Character calculateCharacterFigureByCharacterIdAndUserItemId(String characterId, String newEquippedItemId, String removedItemId) {
        return null;
    }

    @Override
    public Character calculateCharacterFigure(Character selectedCharacter, UserItem newEquippedItem, UserItem removedItem) {
        AttackFigure newEquippedItemAtk = newEquippedItem.getAttackFigure();
        DefenseFigure newEquippedItemDef = newEquippedItem.getDefenseFigure();

        AttackFigure removedItemAtk = removedItem.getAttackFigure();
        DefenseFigure removedItemDef = removedItem.getDefenseFigure();

        AttackFigure charAtk = selectedCharacter.getAttackFigure();
        DefenseFigure charDef = selectedCharacter.getDefenseFigure();

        charAtk.subtract(removedItemAtk);
        charAtk.add(newEquippedItemAtk);

        charDef.subtract(removedItemDef);
        charDef.add(newEquippedItemDef);

        long combatPower = calculateCombatPower(charAtk, charDef);

        selectedCharacter.setCombatPower(combatPower);

        return selectedCharacter;
    }

    private long calculateCombatPower(AttackFigure atk, DefenseFigure def) {
        int dmg = atk.getDamage();
        double crit = atk.getCriticalChance();
        int pen = atk.getArmorPenetration();
        double lifeStl = atk.getLifeStealPercentage();
        double healReduce = atk.getHealingReducePercentage();
        int amr = def.getArmor();
        int hp = def.getHealthPoint();
        double hpRegen = def.getHealingSpeed();
        int cdr = def.getCooldownReducePercentage();
        int mp = def.getManaPoint();

        double atkWeight = Double.parseDouble(ConstantValue.SYSTEM_CONFIG.get(SystemConfigCriteria.ATTACK_WEIGHT.name()));
        double defWeight = Double.parseDouble(ConstantValue.SYSTEM_CONFIG.get(SystemConfigCriteria.DEFENSE_WEIGHT.name()));
        double mgWeight = Double.parseDouble(ConstantValue.SYSTEM_CONFIG.get(SystemConfigCriteria.MAGIC_WEIGHT.name()));

        return (long) ((dmg + crit + pen + lifeStl + healReduce) * atkWeight + (amr + hp + hpRegen) * defWeight + (cdr + mp) * mgWeight);
    }
}
