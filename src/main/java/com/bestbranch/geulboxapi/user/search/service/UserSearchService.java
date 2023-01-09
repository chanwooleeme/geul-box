package com.bestbranch.geulboxapi.user.search.service;

import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.repository.UserSpringDataJpaRepository;
import com.bestbranch.geulboxapi.user.withdrawal.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSearchService {
    private final UserSpringDataJpaRepository userRepository;

    @Transactional(readOnly = true)
    public User getUser(Long userNo) {
        return userRepository.findById(userNo).orElseThrow(UserNotFoundException::new);
    }
}
