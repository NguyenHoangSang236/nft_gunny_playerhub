package com.nftgunny.playerhub.usecases.character;

import com.nftgunny.core.common.usecase.UseCase;
import com.nftgunny.core.config.constant.ConstantValue;
import com.nftgunny.core.config.constant.ResponseResult;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.utils.JwtUtils;
import com.nftgunny.playerhub.config.constant.ItemType;
import com.nftgunny.playerhub.config.constant.UserItemStatus;
import com.nftgunny.playerhub.entities.database.Character;
import com.nftgunny.playerhub.entities.database.UserItem;
import com.nftgunny.playerhub.entities.request.EquipItemRequest;
import com.nftgunny.playerhub.infrastructure.repository.CharacterRepository;
import com.nftgunny.playerhub.infrastructure.repository.UserItemRepository;
import com.nftgunny.playerhub.services.FigureCalculationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class ItemEquipmentUseCase extends UseCase<ItemEquipmentUseCase.InputValue, ApiResponse>{
    final CharacterRepository characterRepo;
    final UserItemRepository userItemRepo;
    final FigureCalculationService figureCalculationService;
    final JwtUtils jwtUtils;

    public ItemEquipmentUseCase(CharacterRepository characterRepo, UserItemRepository userItemRepo, FigureCalculationService figureCalculationService, JwtUtils jwtUtils) {
        this.characterRepo = characterRepo;
        this.userItemRepo = userItemRepo;
        this.figureCalculationService = figureCalculationService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ApiResponse execute(InputValue input) {
        String availableItemId = input.request().getAvailableItemId();
        String equippedItemId = input.request().getEquippedItemId();
        String curUserName = jwtUtils.getTokenInfoFromHttpRequest(input.httpServletRequest()).getUserName();
        UserItem equippedItem = null;
        UserItem availableItem;

        Optional<Character> characterOptional = characterRepo.findByUserName(curUserName);

        if(characterOptional.isEmpty()) {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("Character of user " + curUserName + " does not exist")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        if(equippedItemId != null && !equippedItemId.isBlank()) {
            List<UserItem> items = userItemRepo.findByIdsAndUserName(
                    List.of(equippedItemId, availableItemId),
                    curUserName
            );

            if(items.size() != 2) {
                return ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message("Equipped item ID or available item ID is invalid or non-existed")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            equippedItem = items.get(0).getStatus().equals(UserItemStatus.EQUIPPED) ? items.get(0) : items.get(1);
            availableItem = items.get(1).getStatus().equals(UserItemStatus.AVAILABLE) ? items.get(1) : items.get(0);

            if(!equippedItem.getItemInfo().getType().equals(availableItem.getItemInfo().getType())) {
                return ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message("Item type of equipped item and available item is mis-matching")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            if(!equippedItem.getStatus().equals(UserItemStatus.EQUIPPED) || !availableItem.getStatus().equals(UserItemStatus.AVAILABLE)) {
                return ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message("Equipped item or available item has invalid status")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            equippedItem.setStatus(UserItemStatus.AVAILABLE);
            availableItem.setStatus(UserItemStatus.EQUIPPED);

            userItemRepo.saveAll(List.of(equippedItem, availableItem));
        }
        else {
            Optional<UserItem> availableItemOptional = userItemRepo.findByUserNameAndUserItemId(curUserName, availableItemId);

            if(availableItemOptional.isEmpty()) {
                return ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message("Available item with ID " + availableItemId + " does not exist")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            availableItem = availableItemOptional.get();

            if(availableItem.getStatus() != UserItemStatus.AVAILABLE) {
                return ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message("Invalid status of available item")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            ItemType availableItemType = availableItem.getItemInfo().getType();
            List<UserItem> equippedItems = userItemRepo.findByUserNameAndStatus(curUserName, UserItemStatus.EQUIPPED);

            Map<String, Integer> remainItemTypeSlot = new HashMap<>();
            remainItemTypeSlot.put(ItemType.HAT.name(), ConstantValue.MAX_HAT_AMOUNT_PER_CHARACTER);
            remainItemTypeSlot.put(ItemType.CLOTHES.name(), ConstantValue.MAX_CLOTHES_AMOUNT_PER_CHARACTER);
            remainItemTypeSlot.put(ItemType.WEAPON.name(), ConstantValue.MAX_WEAPON_AMOUNT_PER_CHARACTER);
            remainItemTypeSlot.put(ItemType.ACCESSORY.name(), ConstantValue.MAX_ACCESSORY_AMOUNT_PER_CHARACTER);

            for(UserItem item : equippedItems) {
                remainItemTypeSlot.compute(
                        item.getItemInfo().getType().name(),
                        (k, currentAmount) -> currentAmount - 1
                );
            }

            if(remainItemTypeSlot.get(availableItemType.name()) < 1) {
                return ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message("Invalid item type to equip")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            availableItem.setStatus(UserItemStatus.EQUIPPED);
            userItemRepo.save(availableItem);
        }

        // recalculate the figures of character
        Character character = characterOptional.get();
        character = figureCalculationService.calculateCharacterFigure(character, availableItem, equippedItem);

        characterRepo.save(character);

        return ApiResponse.builder()
                .result(ResponseResult.success.name())
                .message("Equipped item successfully")
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(EquipItemRequest request, HttpServletRequest httpServletRequest) implements UseCase.InputValue {};
}
