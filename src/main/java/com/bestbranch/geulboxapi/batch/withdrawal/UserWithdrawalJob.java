package com.bestbranch.geulboxapi.batch.withdrawal;

import com.bestbranch.geulboxapi.user.withdrawal.service.UserWithdrawalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserWithdrawalJob {
    private final UserWithdrawalService userWithdrawalService;

    //2022-09-08 부터 실행
    //@Scheduled(cron = "* * * * * *")
    //last_login_date 1년 경과 또는 is null 인경우 탈퇴 처리
    public void notifyWithdrawalUnusedUser() {
        userWithdrawalService.notifyWithdrawalUnusedUser();
    }
    
    //2022-08-08 부터 실행
    //@Scheduled(cron = "* * * * * *")
    //last_login_date 1년 경과 또는 is null 인경우 30일전에 고지
    public void notifyWithdrawalUnusedUserInAdvance() {
        userWithdrawalService.notifyWithdrawalUnusedUserInAdvance();
    }
}
