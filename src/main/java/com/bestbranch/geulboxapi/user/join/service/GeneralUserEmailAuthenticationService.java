package com.bestbranch.geulboxapi.user.join.service;

import com.bestbranch.geulboxapi.common.cache.CacheKeyNames;
import com.bestbranch.geulboxapi.common.cache.EhCacheService;
import com.bestbranch.geulboxapi.common.component.mail.EmailAuthenticationMailSender;
import com.bestbranch.geulboxapi.common.exception.model.EmailAlreadyExistsException;
import com.bestbranch.geulboxapi.common.utils.AuthCodeGenerator;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.join.exception.EmailAuthenticationException;
import com.bestbranch.geulboxapi.user.repository.UserSpringDataJpaRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GeneralUserEmailAuthenticationService {
    private final EmailAuthenticationMailSender emailSender;
    private final EhCacheService ehCacheService;
    private final UserSpringDataJpaRepository userRepository;

    public GeneralUserEmailAuthenticationService(EmailAuthenticationMailSender emailSender, EhCacheService ehCacheService,
                                                 UserSpringDataJpaRepository userRepository) {
        this.emailSender = emailSender;
        this.ehCacheService = ehCacheService;
        this.userRepository = userRepository;
    }

    @Transactional
    public String sendAuthenticationMailAndGetAuthenticationKey(String email) throws EmailAlreadyExistsException {
        if (!userRepository.findByEmailAndUserAuthenticationType(email, User.AuthenticationType.GENERAL).isPresent()) {
            String authenticationKey = AuthCodeGenerator.generateUUID();
            ehCacheService.set(CacheKeyNames.EMAIL_AUTHENTICATION_WAITING, authenticationKey, email);
            emailSender.send(email, authenticationKey);
            return authenticationKey;
        }
        throw new EmailAlreadyExistsException("이미 가입된 이메일이 존재합니다.");
    }

    public void authenticateEmail(String authenticationKey) throws EmailAuthenticationException {
        String email = ehCacheService.get(CacheKeyNames.EMAIL_AUTHENTICATION_WAITING, authenticationKey, String.class);
        if (StringUtils.isNotBlank(email)) {
            ehCacheService.delete(CacheKeyNames.EMAIL_AUTHENTICATION_WAITING, authenticationKey);
            ehCacheService.set(CacheKeyNames.EMAIL_AUTHENTICATION_CONFIRMATION, authenticationKey, email);
        } else {
            throw new EmailAuthenticationException();
        }
    }
}
