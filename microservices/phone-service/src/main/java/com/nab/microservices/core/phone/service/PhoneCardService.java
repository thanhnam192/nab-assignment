package com.nab.microservices.core.phone.service;


import com.nab.microservices.core.phone.dto.PhoneCardDto;
import com.nab.microservices.core.phone.dto.PhoneCardOrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface PhoneCardService {
    @PostMapping("/card/buy")
    ResponseEntity<PhoneCardDto> buyPhoneCard(@Valid  @RequestBody PhoneCardOrderDto phoneCardOrderDto);

    @GetMapping("/card/{orderId}")
    ResponseEntity<PhoneCardDto> getPhoneCard(@PathVariable String orderId);
}
