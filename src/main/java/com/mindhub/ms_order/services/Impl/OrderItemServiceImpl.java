package com.mindhub.ms_order.services.Impl;

import com.mindhub.ms_order.dtos.OrderItemDTO;
import com.mindhub.ms_order.exceptions.NotFoundException;
import com.mindhub.ms_order.exceptions.NotValidArgumentException;
import com.mindhub.ms_order.mappers.OrderItemMapper;
import com.mindhub.ms_order.models.OrderEntity;
import com.mindhub.ms_order.models.OrderItem;
import com.mindhub.ms_order.repositories.OrderItemRepository;
import com.mindhub.ms_order.services.OrderEntityService;
import com.mindhub.ms_order.services.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    OrderEntityService orderEntityService;

    @Autowired
    RestTemplate restTemplate;


    @Override
    public OrderItemDTO getOrderItem(Long id) throws NotFoundException {
        try {
            return orderItemMapper.orderItemToDTO(findById(id));
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public List<OrderItemDTO> getAllOrderItems() {
        return orderItemMapper.orderItemListToDTO(orderItemRepository.findAll());
    }

    @Override
    public OrderItem createOrderItem(OrderItemDTO newOrderItem) throws NotFoundException, NotValidArgumentException {
        try {
            isValidProductId(newOrderItem.getProductId());
            isValidProductQuantity(newOrderItem.getProductId(), newOrderItem.getQuantity());
            OrderEntity orderEntity = orderEntityService.getOrderEntity(newOrderItem.getOrderId());
            return save(orderItemMapper.orderItemToEntity(orderEntity, newOrderItem));
        } catch (NotFoundException | NotValidArgumentException e) {
            throw e;
        }
    }

    @Override
    public OrderItem updateOrderItem(Long id, OrderItemDTO updatedOrderItem) throws NotFoundException {
        try {
            OrderItem orderItemToUpdate = findById(id);
            return save(orderItemMapper.updateOrderItemToEntity(orderItemToUpdate, updatedOrderItem));
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public void deleteOrderItem(Long id) throws NotFoundException {
        try {
            existsById(id);
            deleteById(id);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
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

    @Override
    public void isValidProductId(Long id) throws NotFoundException {
        String url = "http://localhost:8082/api/products/" + id;
        try {
            restTemplate.exchange(url, HttpMethod.GET, null, Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException("Product with ID: " + id + " not found.");
        }
    }

    @Override
    public void isValidProductQuantity(Long id, Integer productQuantity) throws NotValidArgumentException {
        String url = "http://localhost:8082/api/products/" + id;
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, null, Map.class);

        Map productData = response.getBody();

        int availableStock = ((Number) productData.get("stock")).intValue();

        if (productQuantity > availableStock) {
            throw new NotValidArgumentException("Insufficient stock for product ID: " + id + ". Requested: " + productQuantity + ", but available: " + availableStock);
        } else {
            updateProductStock(id, availableStock, productQuantity);
        }

    }

    public void updateProductStock(Long id, Integer availableStock, Integer productQuantity) {
        String url = "http://localhost:8082/api/products/" + id;

        Integer newStock = availableStock - productQuantity;

        Map<String, Object> patchBody = new HashMap<>();
        patchBody.put("stock", newStock);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(patchBody);

        restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, Map.class);
    }
}
