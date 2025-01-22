package com.mindhub.ms_order.repositories;

import com.mindhub.ms_order.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
