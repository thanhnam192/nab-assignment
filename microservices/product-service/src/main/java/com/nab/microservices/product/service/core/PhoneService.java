package com.nab.microservices.product.service.core;

import com.nab.microservices.product.dto.AuthenticationDto;
import com.nab.microservices.product.dto.PhoneVerificationDto;
import com.nab.microservices.product.dto.VoucherDto;
import com.nab.microservices.product.dto.VoucherOrderDto;

import java.util.List;

public interface PhoneService {
    VoucherDto buyVoucher(VoucherOrderDto voucherOrderDto);
    VoucherDto getVoucher(String orderId);
    PhoneVerificationDto smsVerification(PhoneVerificationDto phoneVerificationDto);
    List<VoucherDto> getAllVouchersWithAuth(AuthenticationDto authenticationDto);

}
