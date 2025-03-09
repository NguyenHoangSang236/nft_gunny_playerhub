package com.nftgunny.playerhub.usecases.character;

import com.nftgunny.core.common.usecase.UseCase;
import com.nftgunny.core.config.constant.ResponseResult;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.utils.JwtUtils;
import com.nftgunny.playerhub.config.constant.UserItemStatus;
import com.nftgunny.playerhub.entities.database.Character;
import com.nftgunny.playerhub.entities.database.UserItem;
import com.nftgunny.playerhub.infrastructure.repository.CharacterRepository;
import com.nftgunny.playerhub.infrastructure.repository.UserItemRepository;
import com.nftgunny.playerhub.services.FigureCalculationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class ItemUnequipmentUseCase extends UseCase<ItemUnequipmentUseCase.InputValue, ApiResponse> {
    final CharacterRepository characterRepository;
    final UserItemRepository userItemRepository;
    final JwtUtils jwtUtils;
    final FigureCalculationService figureCalculationService;

    public ItemUnequipmentUseCase(CharacterRepository characterRepository, UserItemRepository userItemRepository, JwtUtils jwtUtils, FigureCalculationService figureCalculationService) {
        this.characterRepository = characterRepository;
        this.userItemRepository = userItemRepository;
        this.jwtUtils = jwtUtils;
        this.figureCalculationService = figureCalculationService;
    }

    @Override
    public ApiResponse execute(InputValue input) {
        String equippedItemId = input.equippedItemId();
        String curUserName = jwtUtils.getTokenInfoFromHttpRequest(input.httpServletRequest()).getUserName();
        log.info("UserName from Token: {}", curUserName);

        Optional<Character> characterOptional = characterRepository.findByUserName(curUserName);
        log.info("Character found: {}", characterOptional.isPresent());
        if (characterOptional.isEmpty()) {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("Character of user " + curUserName + " does not exist")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        if (equippedItemId == null || equippedItemId.isBlank()) {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("Equipped item ID is required")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Optional<UserItem> equippedItemOptional = userItemRepository.findByUserNameAndUserItemId(curUserName, equippedItemId);
        if (equippedItemOptional.isEmpty() || !equippedItemOptional.get().getStatus().equals(UserItemStatus.EQUIPPED)) {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("Item is not currently equipped")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        UserItem equippedItem = equippedItemOptional.get();
        //Reset item status from EQUIPPED → AVAILABLE.
        equippedItem.setStatus(UserItemStatus.AVAILABLE);
        userItemRepository.save(equippedItem);


        Character character = characterOptional.get();

        character = figureCalculationService.calculateCharacterFigure(character, null, equippedItem);
        characterRepository.save(character);

        return ApiResponse.builder()
                .result(ResponseResult.success.name())
                .message("Unequipped item successfully")
                .status(HttpStatus.OK)
                .build();

    }


    public record InputValue(String equippedItemId, HttpServletRequest httpServletRequest) implements UseCase.InputValue {}
}
