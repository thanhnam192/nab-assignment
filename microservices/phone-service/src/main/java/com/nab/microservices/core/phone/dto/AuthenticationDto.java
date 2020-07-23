package com.nab.microservices.core.phone.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AuthenticationDto {
    @NotBlank
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$")
    @Size(min = 10)
    private String phoneNumber;

    @NotBlank
    @Size(min = 6, max = 6)
    private String code;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
