package com.nab.microservices.core.phone.persistence;


import com.nab.microservices.core.phone.enums.VoucherOrderStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "voucher")
public class Voucher {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "order_id", unique = true)
    private String orderId;

    @Column(name = "voucher_code")
    private String voucherCode;

    @Enumerated(EnumType.STRING)
    private VoucherOrderStatus status;
    private Timestamp timestamp;

    @Column(name = "mobile_network", nullable = false)
    private String mobileNetwork;

    @Column(nullable = false)
    private BigDecimal price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public VoucherOrderStatus getStatus() {
        return status;
    }

    public void setStatus(VoucherOrderStatus status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
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
}
