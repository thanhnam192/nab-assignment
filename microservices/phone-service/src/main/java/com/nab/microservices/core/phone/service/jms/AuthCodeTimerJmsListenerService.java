package com.nab.microservices.core.phone.service.jms;

import com.nab.microservices.core.phone.dto.AuthCodeTimerSQSDto;
import com.nab.microservices.core.phone.dto.VoucherOrderSQSDto;
import com.nab.microservices.core.phone.logic.PhoneVerificationLogic;
import com.nab.microservices.core.phone.logic.VoucherLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.io.IOException;

@Component
public class AuthCodeTimerJmsListenerService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthCodeTimerJmsListenerService.class);

    @Autowired
    private PhoneVerificationLogic phoneVerificationLogic;

    @JmsListener(destination = "${application.queue.authTimerExpired}")
    public void updateVoucher(String responseJSON) throws Exception {
        LOG.info("Received Order Result : " + responseJSON);
        try {
            AuthCodeTimerSQSDto authCodeTimerSQSDto = AuthCodeTimerSQSDto.fromJSON(responseJSON);
            LOG.info("Aut Code expiered for " + authCodeTimerSQSDto.getPhoneNumber());
            phoneVerificationLogic.resetAuthCode(authCodeTimerSQSDto.getPhoneNumber());
        } catch (IOException ex) {
            LOG.error("Encountered error while parsing message.",ex);
            throw new JMSException("Encountered error while parsing message.");
        }
    }
}
