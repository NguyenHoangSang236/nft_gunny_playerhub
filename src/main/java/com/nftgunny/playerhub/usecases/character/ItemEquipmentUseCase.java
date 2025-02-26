package com.nftgunny.playerhub.usecases.character;

import com.nftgunny.core.common.usecase.UseCase;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.playerhub.infrastructure.repository.CharacterRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemEquipmentUseCase extends UseCase<ItemEquipmentUseCase.InputValue, ApiResponse>{
    final CharacterRepository characterRepo;

    public ItemEquipmentUseCase(CharacterRepository characterRepo) {
        this.characterRepo = characterRepo;
    }

    @Override
    public ApiResponse execute(InputValue input) {


        return null;
    }

    public record InputValue(List<String> itemIds, boolean isEquipped) implements UseCase.InputValue {};
}
