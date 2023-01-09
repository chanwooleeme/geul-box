package com.bestbranch.geulboxapi.common.component.mail;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordInitializationEmailSender {
    private final EmailSender emailSender;
    private final TemplateEngine thymeleaf;
    private final static String SUBJECT = "글을담다 인증번호를 확인해주세요.";

    public void send(String receiverAddress, String authNo) {
        emailSender.send(receiverAddress, getEmailContent(authNo), SUBJECT);
    }

    private String getEmailContent(String authNo) {
        Context context = new Context();
        context.setVariable("authNo", authNo);
        return thymeleaf.process("password-initialization.html", context);
    }
}
