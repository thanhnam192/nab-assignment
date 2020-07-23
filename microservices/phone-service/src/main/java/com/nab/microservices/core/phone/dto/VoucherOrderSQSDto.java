package com.nab.microservices.core.phone.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nab.microservices.core.phone.enums.MockProcessSpeed;
import com.nab.microservices.core.phone.enums.VoucherOrderStatus;
import com.nab.microservices.core.phone.persistence.Voucher;

import java.io.IOException;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VoucherOrderSQSDto {
    private String phoneNumber;
    private String orderId;
    private String mobileNetwork;
    private BigDecimal price;
    private String voucherCode;
    private VoucherOrderStatus status;
    private String message;
    private MockProcessSpeed mockSpeed = MockProcessSpeed.fast;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public VoucherOrderStatus getStatus() {
        return status;
    }

    public void setStatus(VoucherOrderStatus status) {
        this.status = status;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public static VoucherOrderSQSDto fromJSON(String json)
            throws JsonProcessingException, IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.readValue(json, VoucherOrderSQSDto.class);
    }

    public String toJSON() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(this);
    }

    public static VoucherOrderSQSDto from(Voucher voucher){
        VoucherOrderSQSDto voucherOrderSQSDto =  new VoucherOrderSQSDto();
        voucherOrderSQSDto.setOrderId(voucher.getOrderId());
        voucherOrderSQSDto.setPhoneNumber(voucher.getPhoneNumber());
        voucherOrderSQSDto.setMobileNetwork(voucher.getMobileNetwork());
        voucherOrderSQSDto.setPrice(voucher.getPrice());

        return voucherOrderSQSDto;
    }
}
