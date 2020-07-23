package com.nab.microservices.core.phone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nab.microservices.core.phone.persistence.Voucher;

import java.math.BigDecimal;
import java.sql.Timestamp;

@JsonInclude(Include.NON_NULL)
public class VoucherDto {
    private String phoneNumber;
    private String voucherCode;
    private String status;
    private String mobileNetwork;
    private BigDecimal price;
    private Timestamp createdAt;
    private String orderId;
    private String message;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public static VoucherDto fromVoucher(Voucher voucher) {
        VoucherDto voucherDto = new VoucherDto();
        voucherDto.setPhoneNumber(voucher.getPhoneNumber());
        voucherDto.setVoucherCode(voucher.getVoucherCode());
        voucherDto.setMobileNetwork(voucher.getMobileNetwork());
        voucherDto.setPrice(voucher.getPrice());
        voucherDto.setStatus(voucher.getStatus().toString());
        voucherDto.setCreatedAt(voucher.getTimestamp());
        voucherDto.setOrderId(voucher.getOrderId());

        return voucherDto;
    }
}
