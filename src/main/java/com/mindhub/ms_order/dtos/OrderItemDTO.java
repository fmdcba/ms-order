package com.mindhub.ms_order.dtos;

import com.mindhub.ms_order.models.OrderItem;

public class OrderItemDTO {

    private Long id;

    private Long orderId;

    private Long productId;

    private Integer quantity;

    public OrderItemDTO(OrderItem orderItem) {
        id = orderItem.getId();
        orderId = orderItem.getOrderId();
        productId = orderItem.getProductId();
        quantity = orderItem.getQuantity();
    }

    public OrderItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
