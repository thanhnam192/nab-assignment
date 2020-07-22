package com.nab.microservices.product.service.core;

import com.nab.microservices.product.dto.PhoneCardDto;
import com.nab.microservices.product.dto.PhoneCardOrderDto;

public interface PhoneService {
    PhoneCardDto buyPhoneCard(PhoneCardOrderDto phoneCardOrderDto);
    PhoneCardDto getPhoneCard(String orderId);
}
