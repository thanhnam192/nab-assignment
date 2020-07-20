package com.nab.microservices.product.service.core;

import com.nab.microservices.product.dto.PhoneCardDto;

public interface PhoneService {
    PhoneCardDto getPhoneCard(String orderId);
}
