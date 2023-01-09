package com.bestbranch.geulboxapi.common.component.mail;

import javax.mail.internet.InternetAddress;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailSender {
	private final JavaMailSender javaMailSender;
	private final static String ADDRESS_FROM = "no-reply@bestbranch.com";
	private final static String NAME_FROM = "글을담다";

	@Async("authMailSendExecutor")
	public void send(String receiverAddress, String mailContent, String title) {
		MimeMessagePreparator preparator = mimeMessage -> {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			helper.setFrom(new InternetAddress(ADDRESS_FROM, NAME_FROM));
			helper.setTo(receiverAddress);
			helper.setSubject(title);
			helper.setText(mailContent, true);
		};
		javaMailSender.send(preparator);
	}
}
