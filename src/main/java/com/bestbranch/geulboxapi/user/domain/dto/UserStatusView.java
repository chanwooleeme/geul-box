package com.bestbranch.geulboxapi.user.domain.dto;

import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.report.domain.UserReport;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class UserStatusView {

    private User.Status status;
    private UserReport userReport;

    public static UserStatusView of(User user, UserReport userReport) {
        return UserStatusView.of(user.getStatus(), userReport);
    }
}
