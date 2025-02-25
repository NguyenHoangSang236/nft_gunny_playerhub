package com.nftgunny.playerhub.entities.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nftgunny.core.entities.api.request.ApiRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserItemRequest extends ApiRequest {
    @JsonProperty("user_id")
    String userId;

    @NotNull
    @NotBlank
    @Size(max = 50)
    @JsonProperty("item_id")
    String itemId;

    @Override
    public Map<String, Object> toMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, Map.class);
    }
}
