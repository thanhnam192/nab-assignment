package com.nab.microservices.core.phone.controller;

import com.nab.microservices.core.phone.dto.PhoneCardDto;
import com.nab.microservices.core.phone.exceptions.InvalidInputException;
import com.nab.microservices.core.phone.service.PhoneService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PhoneController implements PhoneService {
    private static final Logger LOG = LoggerFactory.getLogger(PhoneController.class);

    @Override
    public PhoneCardDto getPhoneCard(String orderId) {
        if( StringUtils.isBlank(orderId) ){
            throw new InvalidInputException("OrderId is required");
        }

        LOG.debug("Get card number for orderId: {}",orderId);
        PhoneCardDto phoneCardDto = new PhoneCardDto();

        phoneCardDto.setCode("123");
        phoneCardDto.setMessage("OK");
        phoneCardDto.setSuccess(true);
        phoneCardDto.setPhoneNumber("312312312");
        return phoneCardDto;
    }
}
