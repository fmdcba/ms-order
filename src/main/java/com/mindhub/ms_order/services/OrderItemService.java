package com.mindhub.ms_order.services;

import com.mindhub.ms_order.dtos.OrderItemDTO;
import com.mindhub.ms_order.exceptions.NotAuthorizedException;
import com.mindhub.ms_order.exceptions.NotFoundException;
import com.mindhub.ms_order.exceptions.NotValidArgumentException;
import com.mindhub.ms_order.models.OrderItem;

import java.util.List;

public interface OrderItemService extends GenericService<OrderItem> {

    OrderItemDTO getOrderItem(Long id) throws NotFoundException, NotAuthorizedException;

    List<OrderItemDTO> getAllOrderItems() throws NotFoundException, NotAuthorizedException;

    OrderItemDTO createOrderItem(OrderItemDTO newOrderItem) throws NotFoundException, NotValidArgumentException;

    OrderItemDTO updateOrderItem(Long id, OrderItemDTO updatedOrderItem) throws NotFoundException, NotAuthorizedException;

    void deleteOrderItem(Long id) throws NotFoundException, NotAuthorizedException;
}
