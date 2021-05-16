package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.service.interfaces.IMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
        message.setFrom(SENDER_EMAIL);
        message.setTo(receiver);
        message.setSubject(String.format("Dear %s %s, please confirm your registration.", firstName, lastName));
        message.setText(getLinkURL() + token);
        mailSender.send(message);
    }

    private String getLinkURL() {
        return CONFIRM_SIGNUP_BASE_FRONT + CONFIRM_SIGNUP_FRONT;
    }
}
