package com.nftgunny.playerhub.infrastructure.controller;

import com.nftgunny.core.common.usecase.UseCaseExecutor;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.entities.api.response.ResponseMapper;
import com.nftgunny.playerhub.entities.request.CreateUserItemRequest;
import com.nftgunny.playerhub.entities.request.RegisterRequest;
import com.nftgunny.playerhub.usecases.character.ItemEquipmentUseCase;
import com.nftgunny.playerhub.usecases.item.CreateNewUserItemUseCase;
import com.nftgunny.playerhub.usecases.user.RegisterUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Validated
@Tag(name = "Item", description = "Operations relating to managing game items")
@RestController
@RequestMapping(value = "/authen/item", consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class ItemController {
    final UseCaseExecutor useCaseExecutor;
    final CreateNewUserItemUseCase createNewUserItemUseCase;
    final ItemEquipmentUseCase itemEquipmentUseCase;

    @Operation(summary = "Create a new item for player")
    @PostMapping("/createNewUserItem")
    public CompletableFuture<ResponseEntity<ApiResponse>> register(
            @RequestBody
            @NotNull
            CreateUserItemRequest request,
            HttpServletRequest httpServletRequest
    ) {
        return useCaseExecutor.execute(
                createNewUserItemUseCase,
                new CreateNewUserItemUseCase.InputValue(request, httpServletRequest),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Equip items for character")
    @PostMapping("/equip")
    public CompletableFuture<ResponseEntity<ApiResponse>> equipItems(
            @RequestParam("available_item_id")
            @NotNull
            String availableItemId,
            @RequestParam("equipped_item_id")
            String equippedItemId,
            HttpServletRequest httpServletRequest
    ) {
        return useCaseExecutor.execute(
                itemEquipmentUseCase,
                new ItemEquipmentUseCase.InputValue(equippedItemId, availableItemId, httpServletRequest),
                ResponseMapper::map
        );
    }
}
