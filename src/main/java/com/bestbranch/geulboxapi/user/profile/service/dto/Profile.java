package com.bestbranch.geulboxapi.user.profile.service.dto;

import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.report.domain.UserReport;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@Getter
public class Profile {
	private String nickname;
	private String introduction;
	private User.Status userStatus;
	private UserReport userReport;
}
