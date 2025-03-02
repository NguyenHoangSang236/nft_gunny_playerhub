package com.nftgunny.playerhub.infrastructure.controller;

import com.nftgunny.core.common.usecase.UseCaseExecutor;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.entities.api.response.ResponseMapper;
import com.nftgunny.playerhub.usecases.character.ChangeCharacterNameUseCase;
import com.nftgunny.playerhub.usecases.character.GetCharacterByIdUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@Validated
@Tag(name = "Character", description = "Operations relating to managing character")
@RestController
@RequestMapping(consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class CharacterController {
    final UseCaseExecutor useCaseExecutor;
    final GetCharacterByIdUseCase getCharacterByIdUseCase;
    final ChangeCharacterNameUseCase changeCharacterNameUseCase;

    @Operation(summary = "Get character infomation by character ID")
    @GetMapping("/authen/character/{id}")
    public CompletableFuture<ResponseEntity<ApiResponse>> getCharacterById(
            @PathVariable("id")
            @Size(min = 1, max = 50)
            @NotBlank String id,
            HttpServletRequest httpServletRequest
    ){
    return useCaseExecutor.execute(
            getCharacterByIdUseCase,
            new GetCharacterByIdUseCase.InputValue(id,httpServletRequest),
            ResponseMapper::map
    );
    }

    @PatchMapping("/authen/character/{id}/changeName")
    public CompletableFuture<ResponseEntity<ApiResponse>> changeCharacterName(
            @Size(min = 1, max = 50)
            @PathVariable("id")  @NotBlank String id,
            @RequestParam("newName") @NotBlank String newName,
            HttpServletRequest httpServletRequest
    ) {
        return useCaseExecutor.execute(
                changeCharacterNameUseCase,
                new ChangeCharacterNameUseCase.InputValue(id,newName,httpServletRequest),
                ResponseMapper::map
        );
    }

}
