package com.nab.microservices.core.phone.controller;


import com.nab.microservices.core.phone.dto.VoucherDto;
import com.nab.microservices.core.phone.dto.VoucherOrderDto;
import com.nab.microservices.core.phone.exceptions.InvalidInputException;
import com.nab.microservices.core.phone.logic.VoucherLogic;
import com.nab.microservices.core.phone.service.VoucherService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class PhoneController implements VoucherService {
    private static final Logger LOG = LoggerFactory.getLogger(PhoneController.class);
    private VoucherLogic voucherLogic;


    public PhoneController(VoucherLogic voucherLogic){
        this.voucherLogic = voucherLogic;
    }

    @Override
    public ResponseEntity<VoucherDto> buyVoucher(VoucherOrderDto voucherOrderDto) {
        VoucherDto voucherDto =  new VoucherDto();
        try {
            voucherDto = this.voucherLogic.createVoucherOrder(voucherOrderDto);
            voucherDto.setMessage("Your voucher order is processing");
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
        VoucherDto voucherDto =  new VoucherDto();
        try {
            voucherDto = voucherLogic.getVoucher(orderId);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            voucherDto.setMessage("Something wrong. Please try again later!");
            return new ResponseEntity<>(voucherDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().body(voucherDto);
    }
}
