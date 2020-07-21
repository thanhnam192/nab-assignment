package com.nab.microservices.product.service.core;

import com.nab.microservices.product.dto.PhoneCardDto;

public interface PhoneService {
    String buyPhoneCard(String phoneNumber);
    PhoneCardDto getPhoneCard(String orderId);
}
