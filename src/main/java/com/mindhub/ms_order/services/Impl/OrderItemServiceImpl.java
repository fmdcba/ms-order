package com.mindhub.ms_order.services.Impl;

import com.mindhub.ms_order.config.JwtUtils;
import com.mindhub.ms_order.dtos.OrderItemDTO;
import com.mindhub.ms_order.exceptions.NotAuthorizedException;
import com.mindhub.ms_order.exceptions.NotFoundException;
import com.mindhub.ms_order.exceptions.NotValidArgumentException;
import com.mindhub.ms_order.mappers.OrderItemMapper;
import com.mindhub.ms_order.models.OrderEntity;
import com.mindhub.ms_order.models.OrderItem;
import com.mindhub.ms_order.repositories.OrderItemRepository;
import com.mindhub.ms_order.services.OrderEntityService;
import com.mindhub.ms_order.services.OrderItemService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private static final Logger log = LoggerFactory.getLogger(OrderItemServiceImpl.class);

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderEntityService orderEntityService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public OrderItemDTO getOrderItem(Long id) throws NotFoundException, NotAuthorizedException {
        try {
            validateIsAdmin();
            return orderItemMapper.orderItemToDTO(findById(id));
        } catch (NotFoundException | NotAuthorizedException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<OrderItemDTO> getAllOrderItems() throws NotFoundException, NotAuthorizedException {
        try {
            validateIsAdmin();
            List<OrderItemDTO> orderItemList = orderItemMapper.orderItemListToDTO(orderItemRepository.findAll());
            validateOrderItemListIsEmpty(orderItemList);
            return orderItemList;
        } catch (NotFoundException | NotAuthorizedException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public OrderItemDTO createOrderItem(OrderItemDTO newOrderItem) throws NotFoundException, NotValidArgumentException {
        try {
            isValidProductId(newOrderItem.getProductId());
            isValidProductQuantity(newOrderItem.getProductId(), newOrderItem.getQuantity());
            OrderEntity orderEntity = orderEntityService.getOrderEntity(newOrderItem.getOrderId());
            OrderItem savedOrderItem = save(orderItemMapper.orderItemToEntity(orderEntity, newOrderItem));
            return orderItemMapper.orderItemToDTO(savedOrderItem);
        } catch (NotFoundException | NotValidArgumentException e) {
            log.warn(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e;
        }
    }

    @Override
    @Transactional
    public OrderItemDTO updateOrderItem(Long id, OrderItemDTO updatedOrderItem) throws NotFoundException, NotAuthorizedException {
        try {
            validateIsAdmin();
            OrderItem orderItemToUpdate = findById(id);
            OrderItem savedOrderItem = save(orderItemMapper.updateOrderItemToEntity(orderItemToUpdate, updatedOrderItem));
            return orderItemMapper.orderItemToDTO(savedOrderItem);
        } catch (NotFoundException | NotAuthorizedException e) {
            log.warn(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteOrderItem(Long id) throws NotFoundException, NotAuthorizedException {
        try {
            validateIsAdmin();
            existsById(id);
            deleteById(id);
        } catch (NotFoundException | NotAuthorizedException e) {
            log.warn(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e;
        }
    }

    @Override
    public OrderItem findById(Long id) throws NotFoundException {
        return (orderItemRepository.findById(id).orElseThrow(() -> new NotFoundException("Order Item not found.")));
    }

    @Override
    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        orderItemRepository.deleteById(id);
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void existsById(Long id) throws NotFoundException {
        if (!orderItemRepository.existsById(id)) {
            throw new NotFoundException("Order item not found.");
        }
    }

    private void validateOrderItemListIsEmpty(List<OrderItemDTO> allOrderItems) throws NotFoundException {
        if (allOrderItems.toArray().length == 0) {
            throw new NotFoundException("No order items found to show.");
        }
    }

    private void isValidProductId(Long id) throws NotFoundException {
        String url = "http://localhost:8080/api/products/" + id;
        try {
            HttpEntity<String> entity = createAuthHttpEntity(null);
            restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException("Product with ID: " + id + " not found.");
        }
    }


    private void isValidProductQuantity(Long id, Integer productQuantity) throws NotValidArgumentException {
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

    private void updateProductStock(Long id, Integer availableStock, Integer productQuantity) {
        String url = "http://localhost:8080/api/products/" + id;

        Integer newStock = availableStock - productQuantity;

        Map<String, Object> patchBody = new HashMap<>();
        patchBody.put("stock", newStock);

        HttpEntity<Map<String, Object>> requestEntity = createAuthHttpEntity(patchBody);

        restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, Map.class);
    }

    @Override
    public <T> HttpEntity<T> createAuthHttpEntity(T body){
        String token = jwtUtils.getJwtToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        if (body != null) {
            return new HttpEntity<>(body, headers);
        } else {
            return new HttpEntity<>(headers);
        }
    }

    @Override
    public void validateIsAdmin() throws NotAuthorizedException {
        String token = jwtUtils.getJwtToken();
        String userRole = jwtUtils.extractRole(token);

        if (!userRole.equals("ADMIN")) {
            throw new NotAuthorizedException("You are not authorized to access this resource.");
        }
    }
}
