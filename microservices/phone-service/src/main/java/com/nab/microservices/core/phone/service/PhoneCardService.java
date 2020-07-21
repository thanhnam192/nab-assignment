package com.nab.microservices.core.phone.service;


import com.nab.microservices.core.phone.dto.PhoneCardDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public interface PhoneCardService {
    @PostMapping("/card/buy/{phoneNumber}")
    ResponseEntity<String> buyPhoneCard(@PathVariable String phoneNumber);

    @GetMapping("/card/{orderId}")
    ResponseEntity<PhoneCardDto> getPhoneCard(@PathVariable String orderId);
}
