package com.mindhub.ms_order.dtos;

import com.mindhub.ms_order.models.OrderEntity;
import com.mindhub.ms_order.models.OrderStatus;

import java.util.List;

public class OrderEntityDTO {

    private Long id;

    private Long userId;

    private List<OrderItemDTO> products;

    private OrderStatus status;

    public OrderEntityDTO(OrderEntity order) {
        id = order.getId();
        userId = order.getUserId();
        products = order.getProducts().stream().map(product -> new OrderItemDTO(product)).toList();
        status = order.getStatus();
    }

    public OrderEntityDTO() {
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public List<OrderItemDTO> getProducts() {
        return products;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
