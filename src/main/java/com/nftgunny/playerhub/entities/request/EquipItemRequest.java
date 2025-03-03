package com.nftgunny.playerhub.entities.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EquipItemRequest {
    @NotBlank
    @JsonProperty("available_item_id")
    private String availableItemId;

    @JsonProperty("equipped_item_id")
    private String equippedItemId;
}
