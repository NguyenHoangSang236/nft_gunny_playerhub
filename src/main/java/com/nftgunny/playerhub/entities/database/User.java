package com.nftgunny.playerhub.entities.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nftgunny.core.config.constant.SystemRole;
import com.nftgunny.core.entities.database.MongoDbEntity;
import com.nftgunny.playerhub.config.constant.UserStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Document("user")
@Builder
public class User extends MongoDbEntity implements UserDetails {
    @Getter
    @Setter
    @Id
    String id;

    @Setter
    @Field(name = "user_name")
    @JsonProperty("user_name")
    @Indexed(unique = true, name = "unique_account_username_index")
    String userName;

    @Setter
    @Field(name = "password")
    @JsonIgnore
    String password;

    @Getter
    @Setter
    @JsonProperty("current_jwt")
    @Field(name = "current_jwt")
    String currentJwt;

    @Getter
    @Setter
    @JsonProperty("wallet_hash_address")
    @Field(name = "wallet_hash_address")
    String walletHashAddress;

    @Getter
    @Setter
    @JsonProperty("coin_amount")
    @Field(name = "coin_amount")
    double coinAmount;

    @Getter
    @Setter
    @JsonProperty("phone_number")
    @Field(name = "phone_number")
    String phoneNumber;

    @Getter
    @Setter
    @JsonProperty("email")
    @Field(name = "email")
    String email;

    @Getter
    @Setter
    @Field(name = "status")
    @Enumerated(EnumType.STRING)
    UserStatus status;


    @Getter
    @Setter
    @Field(name = "role")
    @Enumerated(EnumType.STRING)
    SystemRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
