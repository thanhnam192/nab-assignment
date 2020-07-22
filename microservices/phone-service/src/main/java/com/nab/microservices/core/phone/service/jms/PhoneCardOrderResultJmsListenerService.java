package com.nab.microservices.core.phone.service.jms;

import com.nab.microservices.core.phone.dto.PhoneCardOrderSQSDto;
import com.nab.microservices.core.phone.logic.PhoneCardLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.io.IOException;

@Component
public class PhoneCardOrderResultJmsListenerService {

    private static final Logger LOG = LoggerFactory.getLogger(PhoneCardOrderResultJmsListenerService.class);

    @Autowired
    private PhoneCardLogic phoneCardLogic;

    @JmsListener(destination = "${application.queue.result}")
    public void createThumbnail(String responseJSON) throws JMSException {
        LOG.info("Received Order Result : " + responseJSON);
        try {
            PhoneCardOrderSQSDto phoneCardOrderSQSDto = PhoneCardOrderSQSDto.fromJSON(responseJSON);
            LOG.info(phoneCardOrderSQSDto.getMessage());
            phoneCardLogic.updatePhoneCardOrderCode(phoneCardOrderSQSDto);
        } catch (IOException ex) {
            LOG.error("Encountered error while parsing message.",ex);
            throw new JMSException("Encountered error while parsing message.");
        }
    }
}
