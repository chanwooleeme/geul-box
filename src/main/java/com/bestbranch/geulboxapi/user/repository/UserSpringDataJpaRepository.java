package com.bestbranch.geulboxapi.user.repository;

import com.bestbranch.geulboxapi.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSpringDataJpaRepository extends JpaRepository<User, Long> {
    List<User> findByLastLoginDateTimeIsNullOrLastLoginDateTimeLessThan(LocalDateTime lastLoginDateTime);

    Optional<User> findByThirdPartyAccountIdAndUserAuthenticationType(String thirdPartyAccountId, User.AuthenticationType userAuthenticationType);

    Optional<User> findByThirdPartyAccountId(String thirdPartyAccountId);

    Optional<User> findByEmailAndUserAuthenticationType(String email, User.AuthenticationType userAuthenticationType);

    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findByAccessToken(String accessToken);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByEmailAndUserAuthenticationTypeAndPassword(String email, User.AuthenticationType userAuthenticationType, String password);
}
