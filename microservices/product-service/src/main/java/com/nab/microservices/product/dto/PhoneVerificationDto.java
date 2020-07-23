package com.nab.microservices.product.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PhoneVerificationDto {
    @NotBlank
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$")
    @Size(min = 10)
    private String phoneNumber;


    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}