package com.mindhub.ms_order.repositories;

import com.mindhub.ms_order.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
