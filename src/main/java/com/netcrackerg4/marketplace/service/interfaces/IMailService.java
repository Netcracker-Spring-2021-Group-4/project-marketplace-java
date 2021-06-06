package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.dto.order.DeliveryDetails;
import com.netcrackerg4.marketplace.model.enums.AccountActivation;

import java.util.UUID;

public interface IMailService {
    void sendConfirmSignupLetter(String email, String firstName, String lastName, UUID token, AccountActivation activationType);

    void sendPasswordResetEmail(String email, UUID token);

    void notifyCourierGotDelivery(String courierEmail, DeliveryDetails deliveryDetails);
}
