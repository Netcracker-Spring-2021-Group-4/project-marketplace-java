package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.service.interfaces.IMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements IMailService {
    @Value("${spring.mail.username}")
    private final String SENDER_EMAIL;
    @Value("${custom.frontend.confirmation-url}")
    private final String CONFIRM_SIGNUP_FRONT;
    private final JavaMailSender mailSender;

    @Override
    public void sendConfirmSignupLetter(String receiver, String firstName, String lastName, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER_EMAIL);
        message.setTo(receiver);
        message.setSubject(String.format("Dear %s %s, please confirm your registration.", firstName, lastName));
        message.setText(CONFIRM_SIGNUP_FRONT + token);
        mailSender.send(message);
    }
}
