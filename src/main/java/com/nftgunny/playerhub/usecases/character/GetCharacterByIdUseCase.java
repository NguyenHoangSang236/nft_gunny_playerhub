package com.nftgunny.playerhub.usecases.character;

import com.nftgunny.core.common.usecase.UseCase;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.playerhub.entities.database.Character;
import com.nftgunny.playerhub.infrastructure.repository.CharacterRepository;
import com.nftgunny.playerhub.usecases.user.LoginUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetCharacterByIdUseCase extends UseCase<GetCharacterByIdUseCase.InputValue, ApiResponse> {

    final CharacterRepository characterRepository;

    public GetCharacterByIdUseCase(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @Override
    public ApiResponse execute(InputValue input) {
        Optional<Character> characterOptional = characterRepository.findById(input.characterId());
        if (characterOptional.isPresent()) {
            Character character = characterOptional.get();
            return ApiResponse.builder()
                    .result("success")
                    .message("Get character successfully")
                    .content(character)
                    .status(HttpStatus.OK)
                    .build();
        }
        else{
            return ApiResponse.builder()
                    .result("error")
                    .message("Character not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    public record InputValue(String characterId) implements UseCase.InputValue {};
}

