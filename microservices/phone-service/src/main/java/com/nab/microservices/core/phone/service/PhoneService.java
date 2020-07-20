package com.nab.microservices.core.phone.service;


import com.nab.microservices.core.phone.dto.PhoneCardDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface PhoneService {
    @GetMapping("/card/{orderId}")
    PhoneCardDto getPhoneCard(@PathVariable String orderId);
}
