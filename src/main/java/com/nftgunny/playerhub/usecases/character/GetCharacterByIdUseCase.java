package com.nftgunny.playerhub.usecases.character;

import com.nftgunny.core.common.usecase.UseCase;
import com.nftgunny.core.config.constant.ResponseResult;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.utils.JwtUtils;
import com.nftgunny.playerhub.entities.database.Character;
import com.nftgunny.playerhub.infrastructure.repository.CharacterRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class GetCharacterByIdUseCase extends UseCase<GetCharacterByIdUseCase.InputValue, ApiResponse> {

    final CharacterRepository characterRepository;
    final JwtUtils jwtUtils;

    public GetCharacterByIdUseCase(CharacterRepository characterRepository, JwtUtils jwtUtils) {
        this.characterRepository = characterRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ApiResponse execute(InputValue input) {

        String curUserName = jwtUtils.getTokenInfoFromHttpRequest(input.httpServletRequest()).getUserName();


        Optional<Character> characterOptional = characterRepository.findById(input.characterId());

        //Kiểm tra xem có character không
        if (characterOptional.isEmpty()) {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("Character not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        Character character = characterOptional.get();

        //Kiểm tra xem nhân vật có thuộc về user
        if (!character.getName().equals(curUserName)) {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("No permisssion")
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }


        return ApiResponse.builder()
                .result(ResponseResult.success.name())
                .message("Get character successfully")
                .content(character)
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(String characterId, HttpServletRequest httpServletRequest) implements UseCase.InputValue {};
}
