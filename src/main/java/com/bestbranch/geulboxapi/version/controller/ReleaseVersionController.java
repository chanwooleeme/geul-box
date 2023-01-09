package com.bestbranch.geulboxapi.version.controller;

import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.version.service.ReleaseVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "배포 버전 API 목록", tags = {"[RELEASE-VERSION]"})
@RestController
@RequiredArgsConstructor
public class ReleaseVersionController {
    private final ReleaseVersionService releaseVersionService;

    @ApiOperation(value = "강제 업데이트 버전 존재 여부 조회")
    @GetMapping("/v1.3/current-release-versions/{currentReleaseVersion}/is-exist-forced-update-release-versions")
    public ApiResponse isExistForcedUpdateReleaseVersions(@PathVariable String currentReleaseVersion) {
        return ApiResponse.success(releaseVersionService.isExistForcedUpdateReleaseVersions(currentReleaseVersion));
    }

    @ApiOperation(value = "강제 업데이트 버전 캐시 evict")
    @PutMapping("/v1.3/release-versions/evict")
    public ApiResponse evictCachingUpdateReleaseVersions() {
        releaseVersionService.evictCachingUpdateReleaseVersions();
        return ApiResponse.success();
    }
}
