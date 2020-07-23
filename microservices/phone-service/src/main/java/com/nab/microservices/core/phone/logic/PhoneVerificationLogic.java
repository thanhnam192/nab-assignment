package com.nab.microservices.core.phone.logic;

import com.nab.microservices.core.phone.dto.AuthCodeTimerSQSDto;
import com.nab.microservices.core.phone.dto.PhoneVerificationDto;
import com.nab.microservices.core.phone.dto.SmsSQSDto;
import com.nab.microservices.core.phone.persistence.PhoneVerification;
import com.nab.microservices.core.phone.persistence.PhoneVerificationRepository;
import com.nab.microservices.core.phone.service.jms.AuthTimerCreationNotification;
import com.nab.microservices.core.phone.service.jms.SMSCreationNotification;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;


@Component
public class PhoneVerificationLogic {
    private static final Logger LOG = LoggerFactory.getLogger(PhoneVerificationLogic.class);
    private PhoneVerificationRepository phoneVerificationRepository;
    private SMSCreationNotification smsCreationNotification;
    private AuthTimerCreationNotification authTimerCreationNotification;

    public PhoneVerificationLogic(PhoneVerificationRepository phoneVerificationRepository,
                                  SMSCreationNotification smsCreationNotification,
                                  AuthTimerCreationNotification authTimerCreationNotification){
        this.phoneVerificationRepository = phoneVerificationRepository;
        this.smsCreationNotification = smsCreationNotification;
        this.authTimerCreationNotification = authTimerCreationNotification;
    }

    public void createSmsAuthCode(PhoneVerificationDto phoneVerificationDto) throws IOException {
        String code = RandomStringUtils.randomAlphanumeric(6);

        PhoneVerification phoneVerification;

        Optional<PhoneVerification> phoneVerificationOptional = phoneVerificationRepository.findFirstByPhoneNumber(phoneVerificationDto.getPhoneNumber());

        if ( phoneVerificationOptional.isPresent() ) {
            phoneVerification = phoneVerificationOptional.get();
        } else {
            phoneVerification =  new PhoneVerification();
            phoneVerification.setPhoneNumber(phoneVerificationDto.getPhoneNumber());
        }

        phoneVerification.setCode(code);
        phoneVerification.setTimestamp(new Timestamp(System.currentTimeMillis()));

        phoneVerificationRepository.save(phoneVerification);

        sendSmsCode(phoneVerification.getCode(), phoneVerification.getPhoneNumber());
        createAuthCodeTimer(phoneVerification.getPhoneNumber());
    }

    private void sendSmsCode(String code, String phoneNumber) throws IOException {
        String message = "Your authenticate code is : " + code;
        SmsSQSDto sms = new SmsSQSDto();
        sms.setPhoneNumber(phoneNumber);
        sms.setMessage(message);

        smsCreationNotification.notificationRequest(sms);
    }

    private void createAuthCodeTimer(String phoneNumber) throws IOException {
        AuthCodeTimerSQSDto authCodeTimerSQSDto = new AuthCodeTimerSQSDto();
        authCodeTimerSQSDto.setPhoneNumber(phoneNumber);
        authTimerCreationNotification.notificationRequest(authCodeTimerSQSDto);

    }

    public void resetAuthCode(String phoneNumber) {
        Optional<PhoneVerification> phoneVerificationOptional = phoneVerificationRepository.findFirstByPhoneNumber(phoneNumber);
        if( !phoneVerificationOptional.isPresent() ) return;

        PhoneVerification phoneVerification = phoneVerificationOptional.get();
        phoneVerification.setCode("");
        phoneVerificationRepository.save(phoneVerification);
    }
}
