package com.nab.microservices.core.phone.service.jms;

import com.nab.microservices.core.phone.dto.VoucherOrderSQSDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class VoucherOrderCreationNotification {

    @Value("${application.queue.order}")
    private String orderRequestQueue;

    @Autowired
    protected JmsTemplate defaultJmsTemplate;

    public void notificationRequest(VoucherOrderSQSDto request) throws IOException {

        defaultJmsTemplate.convertAndSend(orderRequestQueue, request.toJSON());
    }
}
