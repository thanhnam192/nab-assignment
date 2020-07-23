package com.nab.microservices.core.phone.controller;


import com.nab.microservices.core.phone.dto.AuthenticationDto;
import com.nab.microservices.core.phone.dto.PhoneVerificationDto;
import com.nab.microservices.core.phone.dto.VoucherDto;
import com.nab.microservices.core.phone.dto.VoucherOrderDto;
import com.nab.microservices.core.phone.exceptions.InvalidInputException;
import com.nab.microservices.core.phone.logic.PhoneVerificationLogic;
import com.nab.microservices.core.phone.logic.VoucherLogic;
import com.nab.microservices.core.phone.service.api.PhoneVerificationService;
import com.nab.microservices.core.phone.service.api.VoucherService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PhoneController implements VoucherService, PhoneVerificationService {
    private static final Logger LOG = LoggerFactory.getLogger(PhoneController.class);
    private VoucherLogic voucherLogic;
    private PhoneVerificationLogic phoneVerificationLogic;


    public PhoneController(VoucherLogic voucherLogic, PhoneVerificationLogic phoneVerificationLogic){
        this.voucherLogic = voucherLogic;
        this.phoneVerificationLogic = phoneVerificationLogic;
    }

    @Override
    public ResponseEntity<VoucherDto> buyVoucher(VoucherOrderDto voucherOrderDto) {
        VoucherDto voucherDto =  new VoucherDto();
        try {
            voucherDto = this.voucherLogic.createVoucherOrder(voucherOrderDto);
            voucherDto.setMessage("Voucher order request is being processed within 30 seconds");
        } catch (IOException e) {
           LOG.error(e.getMessage());
            voucherDto.setMessage("Create voucher order failed");
            return new ResponseEntity<>(voucherDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().body(voucherDto);
    }

    @Override
    public ResponseEntity<VoucherDto> getVoucher(String orderId) {
        if( StringUtils.isBlank(orderId) ){
            throw new InvalidInputException("OrderId is required");
        }

        LOG.debug("Get voucher code for orderId: {}",orderId);
        VoucherDto voucherDto = voucherLogic.getVoucher(orderId);

        return ResponseEntity.ok().body(voucherDto);
    }

    @Override
    public ResponseEntity<List<VoucherDto>> getAllVouchersWithAuth(@Valid AuthenticationDto authenticationDto) {
        List<VoucherDto> vouchers = voucherLogic.getAllVouchersWithAuth(authenticationDto);

        return ResponseEntity.ok().body(vouchers);
    }

    @Override
    public ResponseEntity<PhoneVerificationDto> sendSmsAuth(@Valid PhoneVerificationDto phoneVerificationDto) {
        try {
            phoneVerificationLogic.createSmsAuthCode(phoneVerificationDto);
            phoneVerificationDto.setMessage("We send sms auth code to your phone. Please check.");
        } catch (IOException e) {
            LOG.error(e.getMessage());
            phoneVerificationDto.setMessage("Something wrong. Please try again later!");
            return new ResponseEntity<>(phoneVerificationDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().body(phoneVerificationDto);
    }
}
