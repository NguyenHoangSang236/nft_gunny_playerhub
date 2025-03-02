package com.nftgunny.playerhub.entities.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nftgunny.core.config.constant.SystemConfigCriteria;
import com.nftgunny.core.entities.api.request.ApiRequest;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class EditSystemConfigRequest extends ApiRequest {
    @NotBlank
    @NotNull
    String description;

    @Size(max = 10)
    @NotBlank
    @NotNull
    String value;

    @Override
    public Map<String, Object> toMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, Map.class);
    }
}
