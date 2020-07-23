package com.nab.microservices.core.phone.persistence;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "phone_verification")
public class PhoneVerification {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    private String code;

    private Timestamp timestamp;

    public Integer getId() {
        return id;
    }

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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
