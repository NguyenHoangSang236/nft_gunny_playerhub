package com.nftgunny.playerhub.usecases.character;

import com.nftgunny.core.common.usecase.UseCase;
import com.nftgunny.core.config.constant.ConstantValue;
import com.nftgunny.core.config.constant.ResponseResult;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.entities.database.TokenInfo;
import com.nftgunny.core.utils.JwtUtils;
import com.nftgunny.playerhub.config.constant.ItemType;
import com.nftgunny.playerhub.config.constant.UserItemStatus;
import com.nftgunny.playerhub.entities.database.UserItem;
import com.nftgunny.playerhub.infrastructure.repository.CharacterRepository;
import com.nftgunny.playerhub.infrastructure.repository.UserItemRepository;
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
    final JwtUtils jwtUtils;

    public ItemEquipmentUseCase(CharacterRepository characterRepo, UserItemRepository userItemRepo, JwtUtils jwtUtils) {
        this.characterRepo = characterRepo;
        this.userItemRepo = userItemRepo;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ApiResponse execute(InputValue input) {
        String availableItemId = input.availableItemId();
        String equippedItemId = input.equippedItemId();
        String curUserName = jwtUtils.getTokenInfoFromHttpRequest(input.httpServletRequest()).getUserName();

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

            UserItem equippedItem = items.get(0);
            UserItem availableItem = items.get(1);

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
            Optional<UserItem> availableItemOptional = userItemRepo.findByUserNameAndItemId(curUserName, availableItemId);

            if(availableItemOptional.isEmpty()) {
                return ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message("Available item with ID " + availableItemId + " does not exist")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            UserItem availableItem = availableItemOptional.get();

            if(availableItem.getStatus() != UserItemStatus.AVAILABLE) {
                return ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message("Invalid status of available item")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            ItemType itemType = availableItem.getItemInfo().getType();
            List<UserItem> equippedItems = userItemRepo.findByUserNameAndStatus(curUserName, UserItemStatus.EQUIPPED);


        }

        // TODO: recalculate the figures of character and save it here...

        return ApiResponse.builder()
                .result(ResponseResult.success.name())
                .message("Create new item successfully")
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(String equippedItemId, String availableItemId, HttpServletRequest httpServletRequest) implements UseCase.InputValue {};
}
