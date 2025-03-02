package com.nftgunny.playerhub.services;

import org.springframework.stereotype.Service;

@Service
public interface FigureCalculationService {
    Character calculateCharacterFigureByUserNamAndUserItemId(String userName, String newEquippedItemId);

    Character calculateCharacterFigureByCharacterIdAndUserItemId(String characterId, String newEquippedItemId);

    Character calculateCharacterFigureByUserItemId(Character selectedCharacter, String newEquippedItemId);
}
