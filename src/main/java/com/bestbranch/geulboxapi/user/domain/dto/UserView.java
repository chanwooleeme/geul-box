package com.bestbranch.geulboxapi.user.domain.dto;

import com.bestbranch.geulboxapi.common.model.JwtToken;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.report.domain.UserReport;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
public class UserView {
    private Long userNo;
    private String email;
    private String name;
    private String nickname;
    private User.AuthenticationType userAuthenticationType;
    private JwtToken jwtToken;
    private String accessToken;
    private String refreshToken;
    private User.Status status;
    private ReportView userReport;

    @Getter
    @AllArgsConstructor(staticName = "of")
    public static class ReportView {
        private Long userReportNo;
        private UserReport.ReportReasonType reportReasonType;
        private String reason;
        private LocalDateTime registerDateTime;
    }

    public static UserView from(User user) {
        return of(user, null, null);
    }

    public static UserView from(User user, UserReport userReport) {
        return of(user, null, userReport);
    }

    public static UserView of(User user, JwtToken jwtToken) {
        return of(user, jwtToken, null);
    }

    public static UserView of(User user, JwtToken jwtToken, UserReport userReport) {
        UserView response = new UserView();
        response.userNo = user.getUserNo();
        response.email = user.getEmail();
        response.name = user.getName();
        response.nickname = user.getNickname();
        response.userAuthenticationType = user.getUserAuthenticationType();
        response.accessToken = user.getAccessToken();
        response.refreshToken = user.getRefreshToken();
        response.jwtToken = jwtToken;
        response.status = user.getStatus();
        if (userReport != null) {
            response.userReport = ReportView.of(userReport.getUserReportNo(), userReport.getReportReasonType(), userReport.getReason(), userReport.getRegisterDateTime());
        }
        return response;
    }
}
