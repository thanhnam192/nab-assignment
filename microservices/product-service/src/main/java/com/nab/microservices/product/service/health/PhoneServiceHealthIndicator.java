package com.nab.microservices.product.service.health;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class PhoneServiceHealthIndicator implements HealthIndicator {
    @Autowired
    private  RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    private final String phoneServiceUrl = "http://phone";

    @Override
    public Health health() {
        try {
            String url = phoneServiceUrl + "/actuator/health";

            ResponseEntity<JsonNode> result = restTemplate.exchange(url, HttpMethod.GET, null , JsonNode.class );

            if (result.getStatusCode().is2xxSuccessful() && result.getBody() != null) {
                Map<String, Object> detail = mapper.convertValue(result.getBody(), new TypeReference<Map<String, Object>>(){});
                return Health.up().withDetails(detail).build();
            } else {
                return Health.down().withDetail("status", result.getStatusCode()).build();
            }

        } catch (Exception ex) {
            return Health.down().withException(ex).build();
        }
    }
}
