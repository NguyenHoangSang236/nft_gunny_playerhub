package com.nftgunny.playerhub.usecases.character;

import com.nftgunny.core.common.usecase.UseCase;
import com.nftgunny.core.config.constant.ResponseResult;
import com.nftgunny.playerhub.entities.database.Character;

import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.playerhub.infrastructure.repository.CharacterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class ChangeCharacterNameUseCase extends UseCase<ChangeCharacterNameUseCase.InputValue, ApiResponse> {
    final CharacterRepository characterRepository;

    public ChangeCharacterNameUseCase(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }
    @Override
    public ApiResponse execute(InputValue input) {

        Optional<Character> characterOptional = characterRepository.findById(input.characterId());

        if (characterOptional.isPresent()) {
            Character character = characterOptional.get();
            character.setName(input.newName());
            characterRepository.save(character);



            return ApiResponse.builder()
                    .result(ResponseResult.success.name())
                    .message("Character name updated successfully")
                    .status(HttpStatus.OK)
                    .build();
        } else {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("Character not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }
    public record InputValue(String characterId, String newName) implements UseCase.InputValue {};
}
