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
    public ResponseEntity<PhoneCardDto> buyPhoneCard(PhoneCardOrderDto phoneCardOrderDto) {
        PhoneCardDto phoneCardDto =  new PhoneCardDto();
        try {
            phoneCardDto = this.phoneCardLogic.createPhoneCardOrder(phoneCardOrderDto);
            phoneCardDto.setMessage("Your order is processing");
        } catch (IOException e) {
           LOG.error(e.getMessage());
            phoneCardDto.setMessage("Created phone card order failed");
            return new ResponseEntity<>(phoneCardDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().body(phoneCardDto);
    }

    @Override
    public ResponseEntity<PhoneCardDto> getPhoneCard(String orderId) {
        if( StringUtils.isBlank(orderId) ){
            throw new InvalidInputException("OrderId is required");
        }

        LOG.debug("Get card number for orderId: {}",orderId);
        PhoneCardDto phoneCardDto =  new PhoneCardDto();
        try {
            phoneCardDto = phoneCardLogic.getPhoneCard(orderId);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            phoneCardDto.setMessage("Something wrong. Please try again later!");
            return new ResponseEntity<>(phoneCardDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().body(phoneCardDto);
    }
}
