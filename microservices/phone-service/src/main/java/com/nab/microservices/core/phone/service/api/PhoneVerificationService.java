package com.nab.microservices.core.phone.service.api;

import com.nab.microservices.core.phone.dto.PhoneVerificationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface PhoneVerificationService {

    @PostMapping("/phone/verification/sms")
    ResponseEntity<PhoneVerificationDto> sendSmsAuth(@Valid @RequestBody PhoneVerificationDto phoneVerificationDto);

}
