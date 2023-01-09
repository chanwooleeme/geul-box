package com.bestbranch.geulboxapi.user.password.service;

import com.bestbranch.geulboxapi.common.cache.CacheKeyNames;
import com.bestbranch.geulboxapi.common.cache.EhCacheService;
import com.bestbranch.geulboxapi.common.component.mail.PasswordInitializationEmailSender;
import com.bestbranch.geulboxapi.common.exception.model.BadRequestException;
import com.bestbranch.geulboxapi.common.exception.model.EmailNotFoundException;
import com.bestbranch.geulboxapi.common.utils.AuthCodeGenerator;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.password.service.dto.PasswordInitializationConfirmationRequest;
import com.bestbranch.geulboxapi.user.password.service.dto.PasswordInitializationRequest;
import com.bestbranch.geulboxapi.user.repository.UserSpringDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordInitializationService {
    private final PasswordInitializationEmailSender emailSender;
    private final EhCacheService ehCacheService;
    private final UserSpringDataJpaRepository userRepository;

    public void sendAuthNo(String email) throws EmailNotFoundException {
        if (userRepository.findByEmailAndUserAuthenticationType(email, User.AuthenticationType.GENERAL).isPresent()) {
            String authNo;
            do {
                authNo = AuthCodeGenerator.generateSixDigitsAuthCode();
            } while (!ehCacheService.isValueEmpty(CacheKeyNames.PASSWORD_INITIALIZATION_AUTHENTICATION_WAITING, authNo));
            emailSender.send(email, authNo);
            ehCacheService.set(CacheKeyNames.PASSWORD_INITIALIZATION_AUTHENTICATION_WAITING, authNo, email);
        } else {
            throw new EmailNotFoundException("해당 이메일로 가입된 정보가 존재하지 않습니다.");
        }
    }

    public boolean confirmAuthNo(PasswordInitializationConfirmationRequest passwordInitializationConfirmationRequest) {
        String emailCache = ehCacheService.get(CacheKeyNames.PASSWORD_INITIALIZATION_AUTHENTICATION_WAITING, passwordInitializationConfirmationRequest.getAuthNo());
        if (passwordInitializationConfirmationRequest.getEmail().equals(emailCache)) {
            ehCacheService.delete(CacheKeyNames.PASSWORD_INITIALIZATION_AUTHENTICATION_WAITING, passwordInitializationConfirmationRequest.getAuthNo());
            ehCacheService.set(CacheKeyNames.PASSWORD_INITIALIZATION_AUTHENTICATION_CONFIRMATION, passwordInitializationConfirmationRequest.getAuthNo(),
                    passwordInitializationConfirmationRequest.getEmail());
            return true;
        }
        return false;
    }

    @Transactional
    public void initializePassword(PasswordInitializationRequest passwordInitializationRequest) throws EmailNotFoundException, BadRequestException {
        String emailCache = ehCacheService.get(CacheKeyNames.PASSWORD_INITIALIZATION_AUTHENTICATION_CONFIRMATION, passwordInitializationRequest.getAuthNo());
        if (passwordInitializationRequest.getEmail().equals(emailCache)) {
            ehCacheService.delete(CacheKeyNames.PASSWORD_INITIALIZATION_AUTHENTICATION_CONFIRMATION, passwordInitializationRequest.getAuthNo());
            User user = userRepository.findByEmailAndUserAuthenticationType(passwordInitializationRequest.getEmail(), User.AuthenticationType.GENERAL).orElseThrow(() -> new EmailNotFoundException("해당 이메일로 가입된 정보가 존재하지 않습니다."));
            user.setPassword(passwordInitializationRequest.getPassword());
        } else {
            throw new BadRequestException("인증 번호 또는 이메일이 올바르지 않습니다.");
        }
    }
}
