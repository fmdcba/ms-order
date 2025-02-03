package com.mindhub.ms_order.utils;

import com.mindhub.ms_order.config.JwtUtils;
import com.mindhub.ms_order.exceptions.NotFoundException;
import com.mindhub.ms_order.exceptions.NotValidArgumentException;
import com.mindhub.ms_order.services.Impl.OrderItemServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class ServiceValidations {

    private static final Logger log = LoggerFactory.getLogger(OrderItemServiceImpl.class);

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private RestTemplate restTemplate;

    public Long getUserId() {
        String token = jwtUtils.getJwtToken();
        return jwtUtils.extractId(token);
    }

    public boolean isAdmin() {
        String token = jwtUtils.getJwtToken();
        String userRole = jwtUtils.extractRole(token);

        return userRole.equals("ADMIN");
    }

    public ResponseEntity<?> isValidProductId(Long id) throws HttpClientErrorException {
        String url = "http://localhost:8080/api/products/" + id;
        HttpEntity<String> entity = createAuthHttpEntity(null);
        try {
            ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            return response;
        } catch (HttpClientErrorException e) {
            log.warn("Error fetching product with id:{}", id);
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }



    public void isValidProductQuantity(Long id, Integer productQuantity) throws NotValidArgumentException {
        String url = "http://localhost:8080/api/products/" + id;
        HttpEntity<String> entity = createAuthHttpEntity(null);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map productData = response.getBody();

        int availableStock = ((Number) productData.get("stock")).intValue();

        if (productQuantity > availableStock) {
            throw new NotValidArgumentException("Insufficient stock for product ID: " + id + ". Requested: " + productQuantity + ", but available: " + availableStock);
        } else {
            updateProductStock(id, availableStock, productQuantity);
        }
    }

    public void updateProductStock(Long id, Integer availableStock, Integer productQuantity) {
        String url = "http://localhost:8080/api/products/" + id;

        Integer newStock = availableStock - productQuantity;

        Map<String, Object> patchBody = new HashMap<>();
        patchBody.put("stock", newStock);

        HttpEntity<Map<String, Object>> requestEntity = createAuthHttpEntity(patchBody);

        restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, Map.class);
    }

    private <T> HttpEntity<T> createAuthHttpEntity(T body) {
        String token = jwtUtils.getJwtToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        if (body != null) {
            return new HttpEntity<>(body, headers);
        } else {
            return new HttpEntity<>(headers);
        }
    }
}
