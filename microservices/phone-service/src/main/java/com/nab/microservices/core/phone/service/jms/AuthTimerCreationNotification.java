package com.nab.microservices.core.phone.service.jms;

import com.nab.microservices.core.phone.dto.AuthCodeTimerSQSDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthTimerCreationNotification {
    @Value("${application.queue.authTimer}")
    private String authTimerQueue;

    @Autowired
    protected JmsTemplate defaultJmsTemplate;

    public void notificationRequest(AuthCodeTimerSQSDto request) throws IOException {

        defaultJmsTemplate.convertAndSend(authTimerQueue, request.toJSON());
    }
}

