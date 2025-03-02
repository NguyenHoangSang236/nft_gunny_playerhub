package com.nftgunny.playerhub.usecases.character;

import com.nftgunny.core.common.usecase.UseCase;
import com.nftgunny.core.config.constant.ResponseResult;
import com.nftgunny.core.utils.JwtUtils;
import com.nftgunny.playerhub.entities.database.Character;

import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.playerhub.infrastructure.repository.CharacterRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ChangeCharacterNameUseCase extends UseCase<ChangeCharacterNameUseCase.InputValue, ApiResponse> {
    final GetCharacterByIdUseCase getCharacterByIdUseCase;
    final CharacterRepository characterRepository;
    final JwtUtils jwtUtils;

    public ChangeCharacterNameUseCase(GetCharacterByIdUseCase getCharacterByIdUseCase, CharacterRepository characterRepository, JwtUtils jwtUtils) {
        this.getCharacterByIdUseCase = getCharacterByIdUseCase;
        this.characterRepository = characterRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ApiResponse execute(InputValue input) {
        ApiResponse response = getCharacterByIdUseCase.execute(new GetCharacterByIdUseCase.InputValue(input.characterId(), input.httpServletRequest()));

        if (!response.getStatus().equals(HttpStatus.OK)) {
            return response;
        }

        Character character = (Character) response.getContent();
        String curUserName = jwtUtils.getTokenInfoFromHttpRequest(input.httpServletRequest()).getUserName();

        if (!character.getName().equals(curUserName)) {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("You do not have permission to change this character's name")
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        character.setName(input.newName());
        characterRepository.save(character);

        return ApiResponse.builder()
                .result(ResponseResult.success.name())
                .message("Character name updated successfully")
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(String characterId, String newName, HttpServletRequest httpServletRequest) implements UseCase.InputValue {}
}
