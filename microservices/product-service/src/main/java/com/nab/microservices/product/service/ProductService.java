package com.nab.microservices.product.service;

import com.nab.microservices.product.dto.PhoneCardDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public interface ProductService {
    @GetMapping(value = "/phone/card/{orderId}")
    ResponseEntity<PhoneCardDto> getPhoneCard(@PathVariable  String orderId);

    @PostMapping(value = "/phone/card/buy/{phoneNumber}")
    ResponseEntity<String> buyPhoneCard(@PathVariable  String phoneNumber);
}
