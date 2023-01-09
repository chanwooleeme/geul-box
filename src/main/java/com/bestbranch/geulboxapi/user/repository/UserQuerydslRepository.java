package com.bestbranch.geulboxapi.user.repository;

import com.bestbranch.geulboxapi.common.infrastructure.DefaultQuerydslRepositorySupport;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.domain.dto.UserSearchRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.bestbranch.geulboxapi.user.domain.QUser.user;


@Repository
public abstract class UserQuerydslRepository extends DefaultQuerydslRepositorySupport {
    @PersistenceContext
    private EntityManager entityManager;

    public UserQuerydslRepository() {
        super(User.class);
    }

    public User getUserBy(Long userNo) {
        return entityManager.find(User.class, userNo);
    }

    public Long getUserNoBy(UserSearchRequest param) {
        User user = getUserBy(param);
        return user != null ? user.getUserNo() : null;
    }

    public User getUserBy(UserSearchRequest param) {
        return from(user)
                .where(eqEmail(param.getEmail()), eqPassword(param.getPassword()), eqUserAuthenticationType(param.getUserAuthenticationType()),
                        eqThirdPartyAccountId(param.getThirdPartyAccountId()), eqAccessToken(param.getAccessToken()),
                        eqRefreshToken(param.getRefreshToken()),
                        eqNickname(param.getNickname()))
                .fetchOne();
    }

    public void saveUser(User user) {
        entityManager.persist(user);
    }

    public boolean isExistUser(UserSearchRequest param) {
        return getUserBy(param) != null;
    }

    public void deleteUserBy(Long userNo) {
        entityManager.remove(entityManager.find(User.class, userNo));
    }

    protected BooleanExpression eqEmail(String email) {
        return email != null ? user.email.eq(email) : null;
    }

    protected BooleanExpression eqUserAuthenticationType(User.AuthenticationType userAuthenticationType) {
        return userAuthenticationType != null ? user.userAuthenticationType.eq(userAuthenticationType) : null;
    }

    protected BooleanExpression eqThirdPartyAccountId(String thirdPartyAccountId) {
        return thirdPartyAccountId != null ? user.thirdPartyAccountId.eq(thirdPartyAccountId) : null;
    }

    protected BooleanExpression eqRefreshToken(String refreshToken) {
        return refreshToken != null ? user.refreshToken.eq(refreshToken) : null;
    }

    protected BooleanExpression eqAccessToken(String accessToken) {
        return accessToken != null ? user.accessToken.eq(accessToken) : null;
    }

    protected BooleanExpression eqNickname(String nickname) {
        return nickname != null ? user.nickname.eq(nickname) : null;
    }

    protected BooleanExpression eqPassword(String password) {
        return password != null ? user.password.eq(password) : null;
    }

}
