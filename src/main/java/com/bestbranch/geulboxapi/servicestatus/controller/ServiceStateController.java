package com.bestbranch.geulboxapi.servicestatus.controller;

import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.servicestatus.domain.ServiceStatus;
import com.bestbranch.geulboxapi.servicestatus.service.ServiceStateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "서비스 상태 API 목록", tags = {"[SERVICE STATUS]"})
@RestController
@RequiredArgsConstructor
public class ServiceStateController {
    private final ServiceStateService serviceStateService;

    @ApiOperation(value = "서비스 상태 조회")
    @GetMapping(value = {"/service-status", "/v1.1/service-status"})
    public ApiResponse<ServiceStatus> getServiceStatus() {
        return ApiResponse.success(serviceStateService.get());
    }
}
