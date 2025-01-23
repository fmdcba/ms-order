package com.mindhub.ms_order.services;

import com.mindhub.ms_order.dtos.OrderItemDTO;
import com.mindhub.ms_order.exceptions.NotFoundException;
import com.mindhub.ms_order.models.OrderItem;

import java.util.List;

public interface OrderItemService extends GenericService<OrderItem> {

    OrderItemDTO getOrderItem(Long id) throws NotFoundException;

    List<OrderItemDTO> getAllOrderItems();

    OrderItem createOrderItem(OrderItemDTO newOrderItem) throws NotFoundException;

    OrderItem updateOrderItem(Long id, OrderItemDTO updatedOrderItem) throws NotFoundException;

    void deleteOrderItem(Long id);
}
