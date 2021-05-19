package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.service.interfaces.IMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements IMailService {
    @Value("${spring.mail.username}")
    private final String SENDER_EMAIL;
    @Value("${custom.frontend.confirmation-url}")
    private final String CONFIRM_SIGNUP_FRONT;
    @Value("${custom.frontend.base-url}")
    private final String CONFIRM_SIGNUP_BASE_FRONT;
    private final JavaMailSender mailSender;

    @Override
    public void sendConfirmSignupLetter(String receiver, String firstName, String lastName, UUID token) {
        SimpleMailMessage message = new SimpleMailMessage();
        String confirmUri = UriComponentsBuilder.fromUriString(CONFIRM_SIGNUP_BASE_FRONT)
                .path(CONFIRM_SIGNUP_FRONT)
                .pathSegment(token.toString())
                .queryParam("requiredPwd", 1)
                .build()
                .encode()
                .toUriString();

        message.setFrom(SENDER_EMAIL);
        message.setTo(receiver);
        message.setSubject(String.format("Dear %s %s, please confirm your registration.", firstName, lastName));
        message.setText(confirmUri);
        mailSender.send(message);
    }
}
