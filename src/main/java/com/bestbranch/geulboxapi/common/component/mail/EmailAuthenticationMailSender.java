package com.bestbranch.geulboxapi.common.component.mail;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class EmailAuthenticationMailSender {
    private final EmailSender emailSender;
    private final TemplateEngine thymeleaf;
    private final static String SUBJECT = "글을담다 이메일을 인증해주세요.";
    @Value("${spring.mail.auth.callback-url}")
    private String callbakUrl;

    public void send(String receiverAddress, String authenticationKey) {
        emailSender.send(receiverAddress, getEmailContent(authenticationKey), SUBJECT);
    }

    private String getEmailContent(String authenticationKey) {
        Context context = new Context();
        context.setVariable("authenticationKey", authenticationKey);
        context.setVariable("callbakUrl", callbakUrl);
        return thymeleaf.process("email-auth.html", context);
    }
}
