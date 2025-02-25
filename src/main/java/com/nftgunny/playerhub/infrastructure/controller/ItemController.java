package com.nftgunny.playerhub.infrastructure.controller;

import com.nftgunny.core.common.usecase.UseCaseExecutor;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.entities.api.response.ResponseMapper;
import com.nftgunny.playerhub.entities.request.CreateUserItemRequest;
import com.nftgunny.playerhub.entities.request.RegisterRequest;
import com.nftgunny.playerhub.usecases.item.CreateNewUserItemUseCase;
import com.nftgunny.playerhub.usecases.user.RegisterUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Validated
@Tag(name = "Item", description = "Operations relating to managing game items")
@RestController
@RequestMapping(value = "/authen/item", consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class ItemController {
    final UseCaseExecutor useCaseExecutor;
    final CreateNewUserItemUseCase createNewUserItemUseCase;

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
}
