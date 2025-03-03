package com.nftgunny.playerhub.services;

import com.nftgunny.playerhub.entities.database.Character;
import com.nftgunny.playerhub.entities.database.UserItem;
import org.springframework.stereotype.Service;

@Service
public interface FigureCalculationService {
    Character calculateCharacterFigureByUserNamAndUserItemId(String userName, String newEquippedItemId, String removedItemId);

    Character calculateCharacterFigureByCharacterIdAndUserItemId(String characterId, String newEquippedItemId, String removedItemId);

    Character calculateCharacterFigure(Character selectedCharacter, UserItem newEquippedItem, UserItem removedItem);
}
