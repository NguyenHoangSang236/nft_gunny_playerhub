package com.nftgunny.playerhub.usecases.item;

import com.nftgunny.core.common.usecase.UseCase;
import com.nftgunny.core.config.constant.ResponseResult;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.entities.database.TokenInfo;
import com.nftgunny.core.utils.JwtUtils;
import com.nftgunny.playerhub.entities.database.Item;
import com.nftgunny.playerhub.entities.database.UserItem;
import com.nftgunny.playerhub.entities.database.dto.UserItemInfoDto;
import com.nftgunny.playerhub.entities.request.CreateUserItemRequest;
import com.nftgunny.playerhub.infrastructure.repository.ItemRepository;
import com.nftgunny.playerhub.infrastructure.repository.UserItemRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CreateNewUserItemUseCase extends UseCase<CreateNewUserItemUseCase.InputValue, ApiResponse> {
    final UserItemRepository userItemRepo;
    final ItemRepository itemRepo;
    final JwtUtils jwtUtils;

    public CreateNewUserItemUseCase(UserItemRepository userItemRepo, ItemRepository itemRepo, JwtUtils jwtUtils) {
        this.userItemRepo = userItemRepo;
        this.itemRepo = itemRepo;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ApiResponse execute(InputValue input) {
        TokenInfo tokenInfo = jwtUtils.getTokenInfoFromHttpRequest(input.httpServletRequest());
        String userName = tokenInfo.getUserName();

        String itemId = input.request().getItemId();

        Optional<Item> itemOptional = itemRepo.findById(itemId);

        if(itemOptional.isEmpty()) {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("Item with ID " + itemId + " does not exist")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        Item item = itemOptional.get();
        Optional<UserItem> userItemOptional = userItemRepo.findByUserNameAndItemId(userName, itemId);

        if(userItemOptional.isPresent()) {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("You have already owned " + item.getName())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        UserItemInfoDto itemInfo = UserItemInfoDto.builder()
                .id(itemId)
                .name(item.getName())
                .imageUrl(item.getImageUrl())
                .type(item.getType())
                .build();
        UserItem newUserItem = UserItem.builder()
                .id(UUID.randomUUID().toString())
                .userName(userName)
                .initialPrice(item.getDefaultPrice())
                .level(1)
                .userItemInfo(itemInfo)
                .defenseFigure(item.getDefaultDefenseFigure())
                .attackFigure(item.getDefaultAttackFigure())
                .build();

        userItemRepo.save(newUserItem);

         return ApiResponse.builder()
                .result(ResponseResult.success.name())
                .message("Create new item successfully")
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(CreateUserItemRequest request, HttpServletRequest httpServletRequest) implements UseCase.InputValue {
    }
}
