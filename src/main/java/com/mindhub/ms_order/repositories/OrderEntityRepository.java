package com.mindhub.ms_order.repositories;

import com.mindhub.ms_order.models.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEntityRepository extends JpaRepository<OrderEntity, Long> {
}
