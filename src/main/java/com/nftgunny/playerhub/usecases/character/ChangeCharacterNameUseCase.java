package com.nftgunny.playerhub.usecases.character;

import com.nftgunny.core.common.usecase.UseCase;
import com.nftgunny.core.config.constant.ResponseResult;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.utils.JwtUtils;
import com.nftgunny.playerhub.entities.database.Character;
import com.nftgunny.playerhub.infrastructure.repository.CharacterRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ChangeCharacterNameUseCase extends UseCase<ChangeCharacterNameUseCase.InputValue, ApiResponse> {
    final CharacterRepository characterRepository;
    final JwtUtils jwtUtils;

    public ChangeCharacterNameUseCase(CharacterRepository characterRepository, JwtUtils jwtUtils) {
        this.characterRepository = characterRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ApiResponse execute(InputValue input) {

        String curUserName = jwtUtils.getTokenInfoFromHttpRequest(input.httpServletRequest()).getUserName();


        Optional<Character> characterOptional = characterRepository.findById(input.characterId());

        //Check if there is a character
        if (characterOptional.isEmpty()) {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("Character not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        Character character = characterOptional.get();


        if (!character.getUserName().equals(curUserName)) {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("You do not have permission to change this character's name")
                    .status(HttpStatus.BAD_REQUEST)
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

    public record InputValue(String characterId, String newName,
                             HttpServletRequest httpServletRequest) implements UseCase.InputValue {
    }
}
