package com.nab.microservices.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nab.microservices.product.dto.PhoneCardDto;
import com.nab.microservices.product.service.core.PhoneService;
import com.nab.microservices.product.util.exceptions.HttpErrorInfo;
import com.nab.microservices.product.util.exceptions.InvalidInputException;
import com.nab.microservices.product.util.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

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
    public PhoneCardDto getPhoneCard(String orderId) {
        try {
            String url = phoneServiceUrl + "/api/card/" + orderId;
            LOG.debug("Will get phone card from URL: {}", url);

            PhoneCardDto phoneCardDto = restTemplate.getForObject(url, PhoneCardDto.class);
            LOG.debug("Card number is: {}", phoneCardDto.getCode());
            LOG.debug("Card number is: {}", phoneCardDto.getMessage());

            return phoneCardDto;

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
