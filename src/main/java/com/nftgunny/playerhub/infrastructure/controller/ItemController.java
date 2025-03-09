package com.nftgunny.playerhub.infrastructure.controller;

import com.nftgunny.core.common.usecase.UseCaseExecutor;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.entities.api.response.ResponseMapper;
import com.nftgunny.playerhub.entities.request.CreateUserItemRequest;
import com.nftgunny.playerhub.entities.request.EquipItemRequest;
import com.nftgunny.playerhub.usecases.character.ItemEquipmentUseCase;
import com.nftgunny.playerhub.usecases.character.ItemUnequipmentUseCase;
import com.nftgunny.playerhub.usecases.item.CreateNewUserItemUseCase;
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
    final ItemUnequipmentUseCase itemUnequipmentUseCase;

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
    @PatchMapping("/equip")
    public CompletableFuture<ResponseEntity<ApiResponse>> equipItems(
            @RequestBody
            EquipItemRequest request,
            HttpServletRequest httpServletRequest
    ) {
        return useCaseExecutor.execute(
                itemEquipmentUseCase,
                new ItemEquipmentUseCase.InputValue(request, httpServletRequest),
                ResponseMapper::map
        );
    }
    @Operation(summary = "Unequip items for character")
    @PatchMapping("/unequip")
    public CompletableFuture<ResponseEntity<ApiResponse>> unequipItems(
           @Size( max = 50)
           @RequestParam("equiped_item_id") String itemId,
            HttpServletRequest httpServletRequest
    ){
       return useCaseExecutor.execute(
            itemUnequipmentUseCase,
               new ItemUnequipmentUseCase.InputValue(itemId, httpServletRequest),
               ResponseMapper::map
       );
    }
}
