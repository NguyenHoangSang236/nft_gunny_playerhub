package com.nftgunny.playerhub.usecases.user;

import com.nftgunny.core.common.usecase.UseCase;
import com.nftgunny.core.config.constant.SystemRole;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.utils.JwtUtils;
import com.nftgunny.playerhub.config.constant.UserStatus;
import com.nftgunny.playerhub.entities.database.User;
import com.nftgunny.playerhub.entities.request.RegisterRequest;
import com.nftgunny.playerhub.infrastructure.repository.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class RegisterUseCase extends UseCase<RegisterUseCase.InputValue, ApiResponse>{
    final UserRepository userRepo;
    final JwtUtils jwtUtils;

    public RegisterUseCase(UserRepository userRepo, JwtUtils jwtUtils) {
        this.userRepo = userRepo;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ApiResponse execute(InputValue input) {
        try {
            RegisterRequest request = input.request();
            User newUser = User.builder()
                    .id(UUID.randomUUID().toString())
                    .userName(request.getUserName())
                    .password(request.getPassword())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .role(SystemRole.PLAYER)
                    .walletHashAddress(request.getWalletHashAddress())
                    .status(UserStatus.OFFLINE)
                    .build();

            newUser.setCreationDate(new Date());

            // TODO: Get coin amount from wallet later

            userRepo.save(newUser);

            jwtUtils.createRefreshTokenForAccount(newUser.getUsername(), newUser.getRole().name());

            return ApiResponse.builder()
                    .result("success")
                    .message("Register successfully")
                    .status(HttpStatus.OK)
                    .build();
        } catch (DuplicateKeyException dupExp) {
            return ApiResponse.builder()
                    .result("failed")
                    .message("This account has been existed")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();

            return ApiResponse.builder()
                    .result("failed")
                    .message("Error")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public record InputValue(RegisterRequest request) implements UseCase.InputValue {
    }
}
