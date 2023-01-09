package com.bestbranch.geulboxapi.version.service;

import com.bestbranch.geulboxapi.common.cache.CacheKeyNames;
import com.bestbranch.geulboxapi.common.cache.EhCacheService;
import com.bestbranch.geulboxapi.version.controller.view.ForcedUpdateReleaseVersionsExistence;
import com.bestbranch.geulboxapi.version.domain.ReleaseVersion;
import com.bestbranch.geulboxapi.version.exception.ReleaseVersionNotFoundException;
import com.bestbranch.geulboxapi.version.repository.ReleaseSpringDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReleaseVersionService {
    private final ReleaseSpringDataJpaRepository releaseVersionRepository;
    private final EhCacheService ehCacheService;
		
    @Transactional(readOnly = true)
    public ForcedUpdateReleaseVersionsExistence isExistForcedUpdateReleaseVersions(String currentReleaseVersion) {
        ReleaseVersion releaseVersion = ehCacheService.get(CacheKeyNames.RELEASE_VERSION, String.valueOf(ReleaseVersion.RELEASE_VERSION_NO),
                ReleaseVersion.class,
                () -> releaseVersionRepository.findById(ReleaseVersion.RELEASE_VERSION_NO).orElseThrow(() -> new ReleaseVersionNotFoundException("Release version not found")));

        if (releaseVersion.minimumReleaseVersionIsLargerThan(currentReleaseVersion)) {
            return ForcedUpdateReleaseVersionsExistence.from(true);
        }
        return ForcedUpdateReleaseVersionsExistence.from(false);
    }

    public void evictCachingUpdateReleaseVersions() {
        ehCacheService.delete(CacheKeyNames.RELEASE_VERSION, String.valueOf(ReleaseVersion.RELEASE_VERSION_NO));
    }
}
