package com.nab.microservices.product.service;

import com.nab.microservices.product.dto.PhoneCardDto;
import com.nab.microservices.product.dto.PhoneCardOrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface ProductService {
    @GetMapping(value = "/phone/card/{orderId}")
    ResponseEntity<PhoneCardDto> getPhoneCard(@PathVariable  String orderId);

    @PostMapping(value = "/phone/card/buy")
    ResponseEntity<String> buyPhoneCard(@Valid @RequestBody PhoneCardOrderDto phoneCardOrderDto);
}
