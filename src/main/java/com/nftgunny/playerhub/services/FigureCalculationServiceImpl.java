package com.nftgunny.playerhub.services;

import com.nftgunny.core.repository.SystemConfigRepository;
import com.nftgunny.playerhub.infrastructure.repository.CharacterRepository;
import org.springframework.stereotype.Service;

@Service
public class FigureCalculationServiceImpl implements FigureCalculationService{
    final SystemConfigRepository configRepo;
    final CharacterRepository characterRepo;

    public FigureCalculationServiceImpl(SystemConfigRepository configRepo, CharacterRepository characterRepo) {
        this.configRepo = configRepo;
        this.characterRepo = characterRepo;
    }


    @Override
    public Character calculateCharacterFigureByUserNamAndUserItemId(String userName, String newEquippedItemId) {
        return null;
    }

    @Override
    public Character calculateCharacterFigureByCharacterIdAndUserItemId(String characterId, String newEquippedItemId) {
        return null;
    }

    @Override
    public Character calculateCharacterFigureByUserItemId(Character selectedCharacter, String newEquippedItemId) {
        return null;
    }
}
