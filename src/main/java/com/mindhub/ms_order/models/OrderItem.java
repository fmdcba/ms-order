package com.mindhub.ms_order.models;

import jakarta.persistence.*;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private OrderEntity orderId;

    private Long productId;

    private Integer quantity;

    public OrderItem(OrderEntity order, Long productId, Integer quantity) {
        this.orderId = order;
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderItem() {
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId.getId();
    }

    public void setOrderId(OrderEntity order) {
        this.orderId = order;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
