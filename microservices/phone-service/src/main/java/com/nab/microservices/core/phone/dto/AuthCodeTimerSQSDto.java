package com.nab.microservices.core.phone.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthCodeTimerSQSDto {
    private String phoneNumber;
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

    public static AuthCodeTimerSQSDto fromJSON(String json)
            throws IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.readValue(json, AuthCodeTimerSQSDto.class);
    }

    public String toJSON() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(this);
    }
}
