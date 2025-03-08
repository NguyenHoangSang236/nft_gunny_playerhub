package com.nftgunny.playerhub.entities.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnequipItemRequest {
    @NotBlank
    private String equippedItemId;
}