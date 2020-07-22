package com.nab.microservices.core.phone.controller;


import com.nab.microservices.core.phone.dto.PhoneCardDto;
import com.nab.microservices.core.phone.dto.PhoneCardOrderDto;
import com.nab.microservices.core.phone.exceptions.InvalidInputException;
import com.nab.microservices.core.phone.logic.PhoneCardLogic;
import com.nab.microservices.core.phone.service.PhoneCardService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class PhoneController implements PhoneCardService {
    private static final Logger LOG = LoggerFactory.getLogger(PhoneController.class);
    private PhoneCardLogic phoneCardLogic;


    public PhoneController(PhoneCardLogic phoneCardLogic){
        this.phoneCardLogic = phoneCardLogic;
    }

    @Override
    public ResponseEntity<String> buyPhoneCard(PhoneCardOrderDto phoneCardOrderDto) {

        try {
            this.phoneCardLogic.createPhoneCardOrder(phoneCardOrderDto);
        } catch (IOException e) {
           LOG.error(e.getMessage());
            return new ResponseEntity<>("Created phone card order failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().body("Create phone card order successfully. You will receive phone card in less than 120 seconds");
    }

    @Override
    public ResponseEntity<PhoneCardDto> getPhoneCard(String orderId) {
        if( StringUtils.isBlank(orderId) ){
            throw new InvalidInputException("OrderId is required");
        }

        LOG.debug("Get card number for orderId: {}",orderId);
        PhoneCardDto phoneCardDto = new PhoneCardDto();

        phoneCardDto.setCode("123");
        phoneCardDto.setMessage("OK");
        phoneCardDto.setSuccess(true);
        phoneCardDto.setPhoneNumber("312312312");
        return ResponseEntity.ok().body(phoneCardDto);
    }
}
