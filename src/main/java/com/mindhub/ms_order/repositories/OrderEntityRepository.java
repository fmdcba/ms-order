package com.mindhub.ms_order.repositories;

import com.mindhub.ms_order.models.OrderEntity;
import com.mindhub.ms_order.models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEntityRepository extends JpaRepository<OrderEntity, Long> {

    boolean existsByUserIdAndStatus(Long userId, OrderStatus status);
}
