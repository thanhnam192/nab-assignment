package com.nab.microservices.core.phone.logic;


import com.nab.microservices.core.phone.dto.PhoneCardOrderDto;
import com.nab.microservices.core.phone.dto.PhoneCardOrderSQSDto;
import com.nab.microservices.core.phone.enums.PhoneCardOrderStatus;
import com.nab.microservices.core.phone.persistence.PhoneCard;
import com.nab.microservices.core.phone.persistence.PhoneCardRepository;
import com.nab.microservices.core.phone.service.jms.PhoneCardOrderCreationNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Component
public class PhoneCardLogic {
    private PhoneCardRepository phoneCardRepository;
    private PhoneCardOrderCreationNotification orderCreationNotification;
    private static final Logger LOG = LoggerFactory.getLogger(PhoneCardLogic.class);

    public PhoneCardLogic(PhoneCardRepository phoneCardRepository, PhoneCardOrderCreationNotification orderCreationNotification){
        this.phoneCardRepository = phoneCardRepository;
        this.orderCreationNotification = orderCreationNotification;
    }

    public void createPhoneCardOrder(PhoneCardOrderDto phoneCardOrderDto) throws IOException {
        PhoneCard phoneCard = new PhoneCard();
        phoneCard.setPhoneNumber(phoneCardOrderDto.getPhoneNumber());
        phoneCard.setMobileNetwork(phoneCardOrderDto.getMobileNetwork());
        phoneCard.setPrice(phoneCardOrderDto.getPrice());

        String orderId = UUID.randomUUID().toString();
        phoneCard.setOrderId(orderId);

        phoneCard.setTimestamp(new Timestamp(System.currentTimeMillis()));

        phoneCard.setStatus(PhoneCardOrderStatus.processing);

        phoneCardRepository.save(phoneCard);
        LOG.info("Created Order");
        LOG.info("Send Order to queue");
        PhoneCardOrderSQSDto orderDto =  new PhoneCardOrderSQSDto();
        orderDto.setOrderId(orderId);
        orderDto.setMobileNetwork(phoneCard.getMobileNetwork());
        orderDto.setPrice(phoneCard.getPrice());
        this.orderCreationNotification.notificationRequest(orderDto);

    }

    public void updatePhoneCardOrderCode(PhoneCardOrderSQSDto phoneCardOrderSQSDto){
        String orderId = phoneCardOrderSQSDto.getOrderId();
        Optional<PhoneCard> phoneCardOptional = phoneCardRepository.findFirstByOrderId(orderId);
        if( !phoneCardOptional.isPresent() ) {
            LOG.error("Can not find phone card information by orderID: " + orderId);
        }

        PhoneCard phoneCard = phoneCardOptional.get();
        phoneCard.setCardNumber(phoneCardOrderSQSDto.getCardNumber());
        phoneCard.setStatus(phoneCardOrderSQSDto.getStatus());

        phoneCardRepository.save(phoneCard);
    }
}
