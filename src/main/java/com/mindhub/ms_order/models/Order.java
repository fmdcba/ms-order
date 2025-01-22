package com.mindhub.ms_order.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> products;

    private OrderStatus status;

    public Order(Long userId, List<OrderItem> products, OrderStatus status) {
        this.userId = userId;
        this.products = products;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItem> getProducts() {
        return products;
    }

    public void setProducts(List<OrderItem> products) {
        this.products = products;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
