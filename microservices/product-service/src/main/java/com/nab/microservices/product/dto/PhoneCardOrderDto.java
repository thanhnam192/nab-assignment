package com.nab.microservices.product.dto;

import com.nab.microservices.product.enums.MockProcessSpeed;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class PhoneCardOrderDto {
    @NotBlank
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$")
    @Size(min = 10)
    private String phoneNumber;
    @NotBlank
    private String mobileNetwork;
    @NotNull
    @DecimalMin("1.00")
    private BigDecimal price;

    private MockProcessSpeed mockSpeed = MockProcessSpeed.fast;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileNetwork() {
        return mobileNetwork;
    }

    public void setMobileNetwork(String mobileNetwork) {
        this.mobileNetwork = mobileNetwork;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public MockProcessSpeed getMockSpeed() {
        return mockSpeed;
    }

    public void setMockSpeed(MockProcessSpeed mockSpeed) {
        this.mockSpeed = mockSpeed;
    }
}

