package com.bestbranch.geulboxapi.user.join.service;

import com.bestbranch.geulboxapi.common.exception.model.AlreadyExistsException;
import com.bestbranch.geulboxapi.common.exception.model.BadRequestException;
import com.bestbranch.geulboxapi.user.domain.dto.UserView;
import com.bestbranch.geulboxapi.user.join.service.dto.UserJoinRequest;

public interface UserJoinService {
    UserView joinUser(UserJoinRequest param) throws AlreadyExistsException, BadRequestException;
}
