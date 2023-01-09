package com.bestbranch.geulboxapi.common.component.mail;

import com.bestbranch.geulboxapi.common.type.Phase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ServerErrorLogMailSender {
    private final EmailSender emailSender;
    private final TemplateEngine thymeleaf;
    private final static String SUBJECT = "\uD83D\uDEA8 글을담다 서버 ERROR";

    @Value("${logging.mail.receiver}")
    private String errorLogReceiver;

    public void send(String errorMessage, String stackTrace, Phase phase, String requestInformation) {
        emailSender.send(errorLogReceiver, getEmailContent(errorMessage, stackTrace, requestInformation), String.format("[%s] %s", phase.toString(), SUBJECT));
    }

    private String getEmailContent(String errorMessage, String stackTrace, String requestInformation) {
        Context context = new Context();
        context.setVariable("errorMessage", Optional.ofNullable(errorMessage).orElse("에러 메시지 없음"));
        context.setVariable("stackTrace", stackTrace);
        context.setVariable("occurrenceDateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        context.setVariable("requestInformation", requestInformation);
        return thymeleaf.process("email-server-error-log.html", context);
    }
}
