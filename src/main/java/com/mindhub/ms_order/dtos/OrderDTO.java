package com.mindhub.ms_order.dtos;

import com.mindhub.ms_order.models.Order;
import com.mindhub.ms_order.models.OrderItem;
import com.mindhub.ms_order.models.OrderStatus;

import java.util.List;

public class OrderDTO {

    private Long id;

    private Long userId;

    private List<OrderItem> products;

    private OrderStatus status;

    public OrderDTO(Order order) {
        id = order.getId();
        userId = order.getUserId();
        products = order.getProducts();
        status = order.getStatus();
    }

    public OrderDTO() {
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public List<OrderItem> getProducts() {
        return products;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
