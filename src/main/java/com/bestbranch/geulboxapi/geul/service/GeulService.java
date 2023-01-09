package com.bestbranch.geulboxapi.geul.service;

import com.bestbranch.geulboxapi.common.cache.EhCacheService;
import com.bestbranch.geulboxapi.common.exception.model.ResourceNotExistsException;
import com.bestbranch.geulboxapi.common.exception.model.UnauthorizedException;
import com.bestbranch.geulboxapi.geul.domain.Geul;
import com.bestbranch.geulboxapi.geul.repository.GeulQuerydslRepository;
import com.bestbranch.geulboxapi.geul.repository.GeulSpringDataJpaRepository;
import com.bestbranch.geulboxapi.geul.service.dto.GeulRegisterRequest;
import com.bestbranch.geulboxapi.geul.service.dto.GeulUpdateRequest;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.repository.UserSpringDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GeulService {
    private final GeulSpringDataJpaRepository geulSpringDataJpaRepository;
    private final GeulQuerydslRepository geulQuerydslRepository;
    private final UserSpringDataJpaRepository userRepository;
    private final EhCacheService ehCacheService;

    @Transactional
    public void registerGeul(GeulRegisterRequest param, Long userNo) throws ResourceNotExistsException {
        User user = userRepository.findById(userNo).orElseThrow(() -> new ResourceNotExistsException("존재하지 않는 회원입니다."));
        Geul geul = Geul.of(param, user);
        geulSpringDataJpaRepository.save(geul);
    }

    @Transactional
    public void updateGeul(Long geulNo, GeulUpdateRequest geulRequest, Long userNo) throws UnauthorizedException, ResourceNotExistsException {
        //ehCacheService.delete(CacheKeyNames.GEUL_DETAIL, String.valueOf(geulNo));
        geulQuerydslRepository.updateGeul(geulNo, geulRequest, userNo);
    }

    @Transactional
    public void removeGeul(Long geulNo, Long userNo) throws UnauthorizedException, ResourceNotExistsException {
        //ehCacheService.delete(CacheKeyNames.GEUL_DETAIL, String.valueOf(geulNo));
        geulQuerydslRepository.removeGeul(geulNo, userNo);
    }
}
