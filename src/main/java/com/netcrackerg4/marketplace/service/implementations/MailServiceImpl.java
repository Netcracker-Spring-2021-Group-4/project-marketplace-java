package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.exception.BadCodeError;
import com.netcrackerg4.marketplace.model.dto.order.DeliveryDetails;
import com.netcrackerg4.marketplace.model.enums.AccountActivation;
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
    @Value("${custom.frontend.base-url}")
    private final String FRONT_BASE_URL;
    @Value("${custom.frontend.email-confirmation-url}")
    private final String CONFIRM_EMAIL_SIGNUP_URL;
    @Value("${custom.frontend.password-confirmation-url}")
    private final String CONFIRM_PASSWORD_SIGNUP_URL;
    @Value("${custom.frontend.password-reset-url}")
    private final String RESET_PASSWORD_URL;
    private final JavaMailSender mailSender;

    @Override
    public void sendConfirmSignupLetter(String receiver, String firstName, String lastName,
                                        UUID token, AccountActivation activationType) {
        SimpleMailMessage message = new SimpleMailMessage();
        String confirmUri = UriComponentsBuilder.fromUriString(FRONT_BASE_URL)
                .path(getConfirmUrl(activationType))
                .pathSegment(token.toString())
                .build()
                .encode()
                .toUriString();

        message.setFrom(SENDER_EMAIL);
        message.setTo(receiver);
        message.setSubject(String.format("Dear %s %s, please confirm your registration.", firstName, lastName));
        message.setText(confirmUri);
        mailSender.send(message);
    }

    private String getConfirmUrl(AccountActivation activationType) {
        if (activationType == AccountActivation.EMAIL) return CONFIRM_EMAIL_SIGNUP_URL;
        if (activationType == AccountActivation.PASSWORD_RESET) return CONFIRM_PASSWORD_SIGNUP_URL;
        else throw new BadCodeError();
    }

    @Override
    public void sendPasswordResetEmail(String receiver, UUID token) {
        SimpleMailMessage message = new SimpleMailMessage();
        String resetUrl = UriComponentsBuilder.fromUriString(FRONT_BASE_URL)
                .path(RESET_PASSWORD_URL)
                .pathSegment(token.toString())
                .build()
                .encode()
                .toUriString();

        message.setFrom(SENDER_EMAIL);
        message.setTo(receiver);
        message.setSubject("NC-Marketplace. Password reset.");
        message.setText(resetUrl);
        mailSender.send(message);
    }

    @Override
    public void notifyCourierGotDelivery(String courierEmail, DeliveryDetails deliveryDetails) {
        // todo: implement
        throw new UnsupportedOperationException();
    }
}