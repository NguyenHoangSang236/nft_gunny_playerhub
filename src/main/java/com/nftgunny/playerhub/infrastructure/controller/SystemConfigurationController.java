package com.nftgunny.playerhub.infrastructure.controller;

import com.nftgunny.core.common.usecase.UseCaseExecutor;
import com.nftgunny.core.config.constant.SystemConfigCriteria;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.entities.api.response.ResponseMapper;
import com.nftgunny.playerhub.entities.request.EditSystemConfigRequest;
import com.nftgunny.playerhub.usecases.user.EditSystemConfigUserCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@Validated
@Tag(name = "Character", description = "Operations relating to managing system configurations")
@RestController
@RequestMapping(value = "/authen/systemConfig", consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class SystemConfigurationController {
    final UseCaseExecutor useCaseExecutor;
    final EditSystemConfigUserCase editSystemConfigUserCase;

    @Operation(summary = "Add new system configuration")
    @PatchMapping("/{id}")
    public CompletableFuture<ResponseEntity<ApiResponse>> createNewSystemConfig(
            @PathVariable("id")
            SystemConfigCriteria id,
            @RequestBody
            EditSystemConfigRequest request
    ) {
        return useCaseExecutor.execute(
                editSystemConfigUserCase,
                new EditSystemConfigUserCase.InputValue(request, id),
                ResponseMapper::map
        );
    }
}
