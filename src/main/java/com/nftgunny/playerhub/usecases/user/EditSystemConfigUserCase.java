package com.nftgunny.playerhub.usecases.user;

import com.nftgunny.core.common.usecase.UseCase;
import com.nftgunny.core.config.constant.ResponseResult;
import com.nftgunny.core.config.constant.SystemConfigCriteria;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.entities.database.SystemConfig;
import com.nftgunny.core.repository.SystemConfigRepository;
import com.nftgunny.core.utils.DbUtils;
import com.nftgunny.playerhub.entities.request.EditSystemConfigRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EditSystemConfigUserCase extends UseCase<EditSystemConfigUserCase.InputValue, ApiResponse>{
    final SystemConfigRepository systemConfigRepo;
    final DbUtils dbUtils;

    public EditSystemConfigUserCase(SystemConfigRepository systemConfigRepo, DbUtils dbUtils) {
        this.systemConfigRepo = systemConfigRepo;
        this.dbUtils = dbUtils;
    }

    @Override
    public ApiResponse execute(InputValue input) {
        EditSystemConfigRequest request = input.request();
        SystemConfigCriteria id = input.id();

        Optional<SystemConfig> systemConfigOptional = systemConfigRepo.findById(id.name());

        if(systemConfigOptional.isEmpty()) {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("System config with ID " + id.name() + " does not exist")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        SystemConfig systemConfig = dbUtils.mergeMongoEntityFromRequest(systemConfigOptional.get(), request);
        systemConfigRepo.save(systemConfig);

        return ApiResponse.builder()
                .result(ResponseResult.success.name())
                .message("Edit system config successfully")
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(EditSystemConfigRequest request, SystemConfigCriteria id) implements UseCase.InputValue {};
}
