package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.exception.BadCodeError;
import com.netcrackerg4.marketplace.model.domain.order.OrderItemEntity;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private final ExecutorService sendExecutor = Executors.newSingleThreadExecutor();

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
        sendEmail(message);
    }

    private void sendEmail(final SimpleMailMessage mail) {
        sendExecutor.submit(() -> mailSender.send(mail));
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
        sendEmail(message);
    }

    @Override
    public void notifyCourierGotDelivery(String courierEmail, DeliveryDetails deliveryDetails) {
        SimpleMailMessage message = new SimpleMailMessage();

        StringBuilder textBuilder = new StringBuilder();
        {
            textBuilder.append("You are assigned a new delivery.").append('\n');

            textBuilder.append("Date: ").append(deliveryDetails.getDatestamp()).append('\n');
            textBuilder.append("Timeslot: ").append(deliveryDetails.getTimeStart())
                    .append(" - ").append(deliveryDetails.getTimeEnd()).append('\n');

            textBuilder.append("Address:\n").append(deliveryDetails.getAddress()).append('\n');

            textBuilder.append("Customer number: ").append(deliveryDetails.getPhoneNumber()).append('\n');
            textBuilder.append("First name: ").append(deliveryDetails.getCustomerFirstName())
                    .append('\t').append("Last name: ").append(deliveryDetails.getCustomerLastName()).append('\n');

            if (deliveryDetails.getComment() != null)
                textBuilder.append("Comment: ").append(deliveryDetails.getComment()).append('\n');

            int sum = 0;
            textBuilder.append("Products:\n");
            for (OrderItemEntity item : deliveryDetails.getOrderItems()) {
                textBuilder.append("ProductNo: ").append(item.getProductId()).append('\n');
                textBuilder.append("Quantity: ").append(item.getQuantity()).append('\n');
                String formattedPrice = String.format("%s.%s", item.getPricePerProduct() / 100,
                        item.getPricePerProduct() - item.getPricePerProduct() / 100 * 100);
                textBuilder.append("Price per item: ").append(formattedPrice).append('\n');
                textBuilder.append('\n');
                sum += item.getPricePerProduct() * item.getQuantity();
            }
            textBuilder.append("Total sum: ").append(sum / 100).append('.').append(sum - sum / 100 * 100);
        }

        message.setFrom(SENDER_EMAIL);
        message.setTo(courierEmail);
        message.setSubject("NC-Marketplace. New order.");
        message.setText(textBuilder.toString());
        sendEmail(message);
    }
}