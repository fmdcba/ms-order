package com.mindhub.ms_order.services;

import com.mindhub.ms_order.dtos.OrderEntityDTO;
import com.mindhub.ms_order.models.OrderEntity;

import java.util.List;

public interface OrderEntityService extends GenericService<OrderEntity> {

    OrderEntityDTO getOrder(Long id) throws Exception;

    OrderEntity getOrderEntity(Long id) throws Exception;

    List<OrderEntityDTO> getAllOrders();

    OrderEntity createOrder(OrderEntityDTO newOrderEntity);

    OrderEntity updateOrder(Long id, OrderEntityDTO updatedOrderEntity) throws Exception;

    void deleteOrder(Long id);
}
