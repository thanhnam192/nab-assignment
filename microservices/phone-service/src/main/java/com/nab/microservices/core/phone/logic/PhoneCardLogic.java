package com.nab.microservices.core.phone.logic;

import com.nab.microservices.core.phone.enums.PhoneCardOrderStatus;
import com.nab.microservices.core.phone.persistence.PhoneCard;
import com.nab.microservices.core.phone.persistence.PhoneCardRepository;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.UUID;

@Component
public class PhoneCardLogic {
    private PhoneCardRepository phoneCardRepository;

    public PhoneCardLogic(PhoneCardRepository phoneCardRepository){
        this.phoneCardRepository = phoneCardRepository;
    }

    public void createPhoneCardOrder(String phoneNumber){
        PhoneCard phoneCard = new PhoneCard();
        phoneCard.setPhoneNumber(phoneNumber);

        String orderId = UUID.randomUUID().toString();
        phoneCard.setOrderId(orderId);

        phoneCard.setTimestamp(new Timestamp(System.currentTimeMillis()));

        phoneCard.setStatus(PhoneCardOrderStatus.processing);

        phoneCardRepository.save(phoneCard);
    }
}
