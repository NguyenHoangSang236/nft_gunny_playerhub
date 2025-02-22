package com.nftgunny.playerhub.usecases.user;

import com.nftgunny.core.common.usecase.UseCase;
import com.nftgunny.core.config.constant.TokenType;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.entities.database.TokenInfo;
import com.nftgunny.core.repository.RefreshTokenRepository;
import com.nftgunny.core.utils.JwtUtils;
import com.nftgunny.playerhub.entities.database.User;
import com.nftgunny.playerhub.infrastructure.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LoginUseCase extends UseCase<LoginUseCase.InputValue, ApiResponse> {
    final JwtUtils jwtUtils;
    final UserRepository userRepo;
    final RefreshTokenRepository refreshTokenRepo;

    public LoginUseCase(JwtUtils jwtUtils, UserRepository userRepo, RefreshTokenRepository refreshTokenRepo) {
        this.jwtUtils = jwtUtils;
        this.userRepo = userRepo;
        this.refreshTokenRepo = refreshTokenRepo;
    }

    @Override
    public ApiResponse execute(InputValue input) {
        Optional<User> userOptional = userRepo.getAccountByUserNameAndPassword(
                input.userName(),
                input.password()
        );
        String newRefreshToken;
        String newJwt;
        TokenInfo tokenInfo;

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Optional<TokenInfo> tokenInfoOptional = refreshTokenRepo.getRefreshTokenInfoByUserName(user.getUsername());

            // if there is any TokenInfo in the database -> get it
            if (tokenInfoOptional.isPresent()) {
                tokenInfo = tokenInfoOptional.get();
            }
            // if there is no TokenInfo in the database -> create a new one
            else {
                tokenInfo = TokenInfo.builder()
                        .id(UUID.randomUUID().toString())
                        .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .userName(user.getUsername())
                        .build();
            }

            // create a new Refresh token
            newRefreshToken = jwtUtils.generateJwt(tokenInfo, TokenType.REFRESH_TOKEN);
            tokenInfo.setToken(newRefreshToken);

            // save to database
            refreshTokenRepo.save(tokenInfo);

            // create a new JWT
            newJwt = jwtUtils.generateJwt(tokenInfo, TokenType.JWT);

            log.info("New Refresh Token: " + newRefreshToken);
            log.info("New JWT: " + newJwt);

            return ApiResponse.builder()
                    .result("success")
                    .message("Login successfully")
                    .jwt(newJwt)
                    .refreshToken(newRefreshToken)
                    .content(user)
                    .status(HttpStatus.OK)
                    .build();
        } else {
            return ApiResponse.builder()
                    .result("failed")
                    .message("Invalid username or password")
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
    }

    public record InputValue(String userName, String password) implements UseCase.InputValue {};
}
