package com.nftgunny.playerhub.entities.database.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nftgunny.playerhub.config.constant.ItemType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserItemInfoDto implements Serializable {
    String id;

    String name;

    @JsonProperty("image_url")
    String imageUrl;

    @Enumerated(EnumType.STRING)
    ItemType type;
}
