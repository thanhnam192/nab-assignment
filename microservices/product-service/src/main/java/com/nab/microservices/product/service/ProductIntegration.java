package com.nab.microservices.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nab.microservices.product.dto.AuthenticationDto;
import com.nab.microservices.product.dto.PhoneVerificationDto;
import com.nab.microservices.product.dto.VoucherDto;
import com.nab.microservices.product.dto.VoucherOrderDto;
import com.nab.microservices.product.util.exceptions.HttpErrorInfo;
import com.nab.microservices.product.util.exceptions.InvalidInputException;
import com.nab.microservices.product.util.exceptions.NotFoundException;
import com.nab.microservices.product.service.core.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Component
public class ProductIntegration implements PhoneService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductIntegration.class);

    private final String phoneServiceUrl = "http://phone";
    private ObjectMapper mapper;
    private final RestTemplate restTemplate;

    public  ProductIntegration(RestTemplate restTemplate, ObjectMapper mapper){
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    @Override
    public VoucherDto buyVoucher(VoucherOrderDto voucherOrderDto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<VoucherOrderDto> httpEntity = new HttpEntity<>(voucherOrderDto, headers);

            String url = phoneServiceUrl + "/api/voucher/buy";
            LOG.debug("Create voucher order from URL: {}", url);

            VoucherDto voucherDto = restTemplate.postForObject(url, httpEntity, VoucherDto.class);

            return voucherDto;

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public VoucherDto getVoucher(String orderId) {
        try {
            String url = phoneServiceUrl + "/api/voucher/" + orderId;
            LOG.debug("Will get voucher from URL: {}", url);

            VoucherDto voucherDto = restTemplate.getForObject(url, VoucherDto.class);

            return voucherDto;

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public PhoneVerificationDto smsVerification(PhoneVerificationDto phoneVerificationDto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PhoneVerificationDto> httpEntity = new HttpEntity<>(phoneVerificationDto, headers);

            String url = phoneServiceUrl + "/api/phone/verification/sms";
            LOG.debug("Send SMS auth from URL: {}", url);

            PhoneVerificationDto phoneVerificationResultDto = restTemplate.postForObject(url, httpEntity, PhoneVerificationDto.class);

            return phoneVerificationResultDto;

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public List<VoucherDto> getAllVouchersWithAuth(AuthenticationDto authenticationDto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<AuthenticationDto> httpEntity = new HttpEntity<>(authenticationDto, headers);

            String url = phoneServiceUrl + "/api/voucher/all";
            LOG.debug("Send SMS auth from URL: {}", url);

            List<VoucherDto> vouchers = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<VoucherDto>>() {}).getBody();


            return vouchers;

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }


    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        switch (ex.getStatusCode()) {

            case NOT_FOUND:
                return new NotFoundException(getErrorMessage(ex));

            case UNPROCESSABLE_ENTITY :
                return new InvalidInputException(getErrorMessage(ex));

            default:
                LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                LOG.warn("Error body: {}", ex.getResponseBodyAsString());
                return ex;
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }
}
