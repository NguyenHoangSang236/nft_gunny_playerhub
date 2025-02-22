package com.nftgunny.playerhub.entities.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nftgunny.core.common.validator.insert.InsertValid;
import com.nftgunny.core.entities.api.request.ApiRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest extends ApiRequest {
    @Setter
    @Getter
    @NotBlank
    @NotNull
    @Size(max = 15)
    @JsonProperty("user_name")
    String userName;

    @Getter
    @Setter
    @InsertValid(
            nullMessage = "Password can not be null",
            sha256Message = "Wrong format of password",
            isSha256 = true
    )
    String password;

    @Getter
    @Setter
    @InsertValid(
            nullMessage = "Phone number can not be null",
            phoneMessage = "Wrong format for phone number",
            isPhoneNumber = true
    )
    @JsonProperty("phone_number")
    String phoneNumber;

    @Getter
    @Setter
    @InsertValid(
            nullMessage = "Email can not be null",
            emailMessage = "Invalid email",
            isEmail = true
    )
    String email;

    @Getter
    @Setter
    @JsonProperty("wallet_hash_address")
    String walletHashAddress;


    @Override
    public Map<String, Object> toMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, Map.class);
    }
}
