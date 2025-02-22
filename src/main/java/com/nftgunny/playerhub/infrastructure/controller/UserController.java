package com.nftgunny.playerhub.infrastructure.controller;

import com.nftgunny.core.common.usecase.UseCaseExecutor;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.entities.api.response.ResponseMapper;
import com.nftgunny.playerhub.entities.request.RegisterRequest;
import com.nftgunny.playerhub.usecases.user.LoginUseCase;
import com.nftgunny.playerhub.usecases.user.RegisterUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@Validated
@Tag(name = "User", description = "Operations relating to managing user")
@RestController
@RequestMapping(consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class UserController {
    final UseCaseExecutor useCaseExecutor;
    final LoginUseCase loginUseCase;
    final RegisterUseCase registerUseCase;

    @Operation(summary = "Login to the system")
    @GetMapping("/unauthen/user/login")
    public CompletableFuture<ResponseEntity<ApiResponse>> login(
            @RequestParam(value = "user_name")
            @NotNull
            @NotBlank
            String userName,
            @RequestParam(value = "password")
            @NotNull
            @NotBlank
            String password
    ) {
        return useCaseExecutor.execute(
                loginUseCase,
                new LoginUseCase.InputValue(userName, password),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Register new user to the system")
    @PostMapping("/unauthen/user/register")
    public CompletableFuture<ResponseEntity<ApiResponse>> register(
            @RequestBody
            @NotNull
            RegisterRequest request
    ) {
        return useCaseExecutor.execute(
                registerUseCase,
                new RegisterUseCase.InputValue(request),
                ResponseMapper::map
        );
    }
}
