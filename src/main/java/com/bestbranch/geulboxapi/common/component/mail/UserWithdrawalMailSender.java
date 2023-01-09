package com.bestbranch.geulboxapi.common.component.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class UserWithdrawalMailSender {
    private final EmailSender emailSender;
    private final TemplateEngine thymeleaf;
    private final static String SUBJECT = "회원 탈퇴 안내 메일입니다.";

    public void send(String receiverAddress) {
        emailSender.send(receiverAddress, getEmailContent(), SUBJECT);
    }

    private String getEmailContent() {
        Context context = new Context();
        return thymeleaf.process("email-withdrawal.html", context);
    }
}
