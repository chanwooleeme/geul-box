package com.bestbranch.geulboxapi.servicestatus.service;

import com.bestbranch.geulboxapi.servicestatus.domain.ServiceStatus;
import com.bestbranch.geulboxapi.servicestatus.repository.ServiceStateSpringDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServiceStateService {
    private final ServiceStateSpringDataJpaRepository serviceStateSpringDataJpaRepository;

    @Transactional(readOnly = true)
    public ServiceStatus get() {
        return serviceStateSpringDataJpaRepository.findById(1L).get();
    }
}
