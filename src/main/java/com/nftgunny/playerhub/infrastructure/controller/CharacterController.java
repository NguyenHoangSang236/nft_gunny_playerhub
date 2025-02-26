package com.nftgunny.playerhub.infrastructure.controller;

import com.nftgunny.core.common.usecase.UseCaseExecutor;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.entities.api.response.ResponseMapper;
import com.nftgunny.playerhub.usecases.character.GetCharacterByIdUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Validated
@Tag(name = "Character", description = "Operations relating to managing character")
@RestController
@RequestMapping(consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class CharacterController {
    final UseCaseExecutor useCaseExecutor;
    final GetCharacterByIdUseCase getCharacterByIdUseCase;

    @Operation(summary = "Get character infomation by character ID")
    @GetMapping("/charater/{id}")
    public CompletableFuture<ResponseEntity<ApiResponse>> getCharacterById(
            @PathVariable("id")
            @NotBlank String characterId
    ){
    return useCaseExecutor.execute(
            getCharacterByIdUseCase,
            new GetCharacterByIdUseCase.InputValue(characterId),
            ResponseMapper::map
    );
    }

}
