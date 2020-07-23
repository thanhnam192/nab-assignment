package com.nab.microservices.product.service;

import com.nab.microservices.product.dto.VoucherDto;
import com.nab.microservices.product.dto.VoucherOrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface ProductService {
    @GetMapping(value = "/phone/voucher/{orderId}")
    ResponseEntity<VoucherDto> getVoucher(@PathVariable  String orderId);

    @PostMapping(value = "/phone/voucher/buy")
    ResponseEntity<VoucherDto> buyVoucher(@Valid @RequestBody VoucherOrderDto voucherOrderDto);
}
