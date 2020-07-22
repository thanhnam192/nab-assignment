package com.nab.microservices.core.phone.service.jms;

import com.nab.microservices.core.phone.dto.SmsSQSDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class SMSCreationNotification {
    @Value("${application.queue.sms}")
    private String smsQueue;

    @Autowired
    protected JmsTemplate defaultJmsTemplate;

    public void notificationRequest(SmsSQSDto request) throws IOException {

        defaultJmsTemplate.convertAndSend(smsQueue, request.toJSON());
    }
}
