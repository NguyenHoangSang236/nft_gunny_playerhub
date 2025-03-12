package com.nftgunny.playerhub.usecases.user;

import com.nftgunny.core.common.usecase.UseCase;
import com.nftgunny.core.config.constant.ConstantValue;
import com.nftgunny.core.config.constant.SystemRole;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.utils.JwtUtils;
import com.nftgunny.playerhub.config.constant.UserStatus;
import com.nftgunny.playerhub.entities.database.dto.AttackFigure;
import com.nftgunny.playerhub.entities.database.Character;
import com.nftgunny.playerhub.entities.database.dto.DefenseFigure;
import com.nftgunny.playerhub.entities.database.User;
import com.nftgunny.playerhub.entities.request.RegisterRequest;
import com.nftgunny.playerhub.infrastructure.repository.CharacterRepository;
import com.nftgunny.playerhub.infrastructure.repository.UserRepository;
import com.nftgunny.playerhub.services.FigureCalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class RegisterUseCase extends UseCase<RegisterUseCase.InputValue, ApiResponse>{
    final UserRepository userRepo;
    final JwtUtils jwtUtils;
    final CharacterRepository characterRepo;
    final FigureCalculationService figureCalculationService;

    public RegisterUseCase(UserRepository userRepo, JwtUtils jwtUtils, CharacterRepository characterRepo, FigureCalculationService figureCalculationService) {
        this.userRepo = userRepo;
        this.jwtUtils = jwtUtils;
        this.characterRepo = characterRepo;
        this.figureCalculationService = figureCalculationService;
    }

    @Override
    public ApiResponse execute(InputValue input) {
        try {
            RegisterRequest request = input.request();
            if (request.getUserName() == null || request.getUserName().trim().isEmpty()) {
                return ApiResponse.builder()
                        .result("failed")
                        .message("Username cannot be empty")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            Boolean exists = userRepo.existsByUserName(request.getUserName());
            if (Boolean.TRUE.equals(exists)) {  // Dùng Boolean.TRUE để tránh lỗi null
                return ApiResponse.builder()
                        .result("failed")
                        .message("This account already exists!")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
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
            userRepo.save(newUser);
            // TODO: Get coin amount from wallet later...

            CompletableFuture<Character> newCharFtr = CompletableFuture.supplyAsync(() -> {
                AttackFigure atk = AttackFigure.builder()
                        .damage(ConstantValue.DEFAULT_CHAR_DAMAGE)
                        .criticalChance(0.0)
                        .lifeStealPercentage(0)
                        .armorPenetration(0)
                        .healingReducePercentage(0)
                        .aimingAngle(0)
                        .build();

                DefenseFigure def = DefenseFigure.builder()
                        .healthPoint(ConstantValue.DEFAULT_CHAR_HP)
                        .manaPoint(ConstantValue.DEFAULT_CHAR_MP)
                        .cooldownReducePercentage(0)
                        .armor(0)
                        .dodgeChance(0.0)
                        .healingSpeed(0)
                        .build();

                long combatPower = figureCalculationService.calculateCombatPower(atk, def);

                // TODO: Get character image and save it here later...

                return Character.builder()
                        .id(UUID.randomUUID().toString())
                        .level(1)
                        .userName(newUser.getUsername())
                        .name(newUser.getUsername())
                        .defenseFigure(def)
                        .attackFigure(atk)
                        .combatPower(combatPower)
                        .build();
            }).exceptionally(ex -> {
                log.error("Error: {}", ex.getMessage());

                return null;
            });


            Character newCharacter = newCharFtr.join();
            characterRepo.save(newCharacter);

            try {
                jwtUtils.createRefreshTokenForAccount(newUser.getUsername(), newUser.getRole().name());
            } catch (DuplicateKeyException e) {
                log.warn("Duplicate refresh token entry for user: {}", newUser.getUsername());
            } catch (Exception e) {
                log.error("Error creating refresh token: {}", e.getMessage());
            }

            return ApiResponse.builder()
                    .result("success")
                    .message("Register successfully")
                    .status(HttpStatus.OK)
                    .build();
        } catch (DuplicateKeyException dupExp) {
            dupExp.printStackTrace();
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