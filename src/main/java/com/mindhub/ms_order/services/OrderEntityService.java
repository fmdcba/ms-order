package com.mindhub.ms_order.services;

import com.mindhub.ms_order.dtos.OrderEntityDTO;
import com.mindhub.ms_order.exceptions.NotFoundException;
import com.mindhub.ms_order.models.OrderEntity;

import java.util.List;

public interface OrderEntityService extends GenericService<OrderEntity> {

    OrderEntityDTO getOrder(Long id) throws NotFoundException;

    OrderEntity getOrderEntity(Long id) throws NotFoundException;

    List<OrderEntityDTO> getAllOrders();

    OrderEntity createOrder(OrderEntityDTO newOrderEntity);

    OrderEntity updateOrder(Long id, OrderEntityDTO updatedOrderEntity) throws NotFoundException;

    void deleteOrder(Long id) throws NotFoundException;
}
