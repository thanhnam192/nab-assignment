package com.nab.microservices.core.phone.logic;


import com.nab.microservices.core.phone.dto.PhoneCardDto;
import com.nab.microservices.core.phone.dto.PhoneCardOrderDto;
import com.nab.microservices.core.phone.dto.PhoneCardOrderSQSDto;
import com.nab.microservices.core.phone.dto.SmsSQSDto;
import com.nab.microservices.core.phone.enums.PhoneCardOrderStatus;
import com.nab.microservices.core.phone.persistence.PhoneCard;
import com.nab.microservices.core.phone.persistence.PhoneCardRepository;
import com.nab.microservices.core.phone.service.jms.PhoneCardOrderCreationNotification;
import com.nab.microservices.core.phone.service.jms.SMSCreationNotification;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Component
public class PhoneCardLogic {
    private static final Logger LOG = LoggerFactory.getLogger(PhoneCardLogic.class);
    private PhoneCardRepository phoneCardRepository;
    private PhoneCardOrderCreationNotification orderCreationNotification;
    private SMSCreationNotification smsCreationNotification;
    @Value("${application.mockSpeed}")
    private boolean mockSpeed;


    public PhoneCardLogic(PhoneCardRepository phoneCardRepository, PhoneCardOrderCreationNotification orderCreationNotification, SMSCreationNotification smsCreationNotification){
        this.phoneCardRepository = phoneCardRepository;
        this.orderCreationNotification = orderCreationNotification;
        this.smsCreationNotification = smsCreationNotification;
    }

    public PhoneCardDto createPhoneCardOrder(PhoneCardOrderDto phoneCardOrderDto) throws IOException {
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
        orderDto.setPhoneNumber(phoneCard.getPhoneNumber());
        if( mockSpeed ) {
            orderDto.setMockSpeed(phoneCardOrderDto.getMockSpeed());
        }

        this.orderCreationNotification.notificationRequest(orderDto);

        return PhoneCardDto.fromPhoneCard(phoneCard);

    }

    public void updatePhoneCardOrderCode(PhoneCardOrderSQSDto phoneCardOrderSQSDto) throws Exception {
        String orderId = phoneCardOrderSQSDto.getOrderId();
        Optional<PhoneCard> phoneCardOptional = phoneCardRepository.findFirstByOrderId(orderId);
        if( !phoneCardOptional.isPresent() ) {
            LOG.error("Can not find phone card information by orderID: " + orderId);
            throw new Exception("Can not find Phone Card Order by OrderId " + orderId);
        }

        PhoneCard phoneCard = phoneCardOptional.get();
        phoneCard.setCardNumber(phoneCardOrderSQSDto.getCardNumber());
        phoneCard.setStatus(phoneCardOrderSQSDto.getStatus());

        phoneCardRepository.save(phoneCard);

        if( phoneCard.getStatus() == PhoneCardOrderStatus.finish ) {
            sendCardNumberSMS(phoneCard);
        }
    }

    private void sendCardNumberSMS(PhoneCard phoneCard) throws IOException {
        SmsSQSDto smsSQSDto = new SmsSQSDto();
        smsSQSDto.setMessage("Your Card Number: " + phoneCard.getCardNumber());
        smsSQSDto.setPhoneNumber(phoneCard.getPhoneNumber());
        this.smsCreationNotification.notificationRequest(smsSQSDto);
    }

    public PhoneCardDto getPhoneCard(String orderId) throws Exception {
        if ( StringUtils.isBlank(orderId) ) return null;

        Optional<PhoneCard> phoneCardOptional = phoneCardRepository.findFirstByOrderId(orderId);
        if( !phoneCardOptional.isPresent() ) {
            LOG.error("Can not find phone card information by orderID: " + orderId);
            throw new Exception("Can not find Phone Card Order by OrderId " + orderId);
        }

        PhoneCard phoneCard = phoneCardOptional.get();
        PhoneCardDto phoneCardDto = PhoneCardDto.fromPhoneCard(phoneCard);

        if( phoneCard.getStatus() == PhoneCardOrderStatus.processing ) {
            phoneCardDto.setMessage("Your card is processing. We will send sms to you when it's finish");
        }


        return phoneCardDto;
    }
}
