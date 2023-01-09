package com.bestbranch.geulboxapi.user.block.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "user_block")
@Getter
public class UserBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_block_no")
    private Long userBlockNo;

    @Column(name = "blocker_user_no")
    private Long blockerUserNo;

    @Column(name = "blocked_user_no")
    private Long blockedUserNo;

    @Column(name = "register_date_time")
    private LocalDateTime registerDateTime;

    @Column(name = "modify_date_time")
    private LocalDateTime modify_date_time;

    protected UserBlock() {
    }

    public static UserBlock of(Long blockerUser, Long blockedUser) {
        UserBlock block = new UserBlock();
        block.blockerUserNo = blockerUser;
        block.blockedUserNo = blockedUser;
        return block;
    }
}
