package com.nftgunny.playerhub.infrastructure.controller;

import com.nftgunny.core.common.usecase.UseCaseExecutor;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "Character", description = "Operations relating to managing character")
@RestController
@RequestMapping(consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class CharacterController {
    final UseCaseExecutor useCaseExecutor;


}
