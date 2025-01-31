package com.mindhub.ms_order.services;

import com.mindhub.ms_order.dtos.OrderEntityDTO;
import com.mindhub.ms_order.exceptions.NotAuthorizedException;
import com.mindhub.ms_order.exceptions.NotFoundException;
import com.mindhub.ms_order.exceptions.NotValidArgumentException;
import com.mindhub.ms_order.models.OrderEntity;

import java.util.List;

public interface OrderEntityService extends GenericService<OrderEntity> {

    OrderEntityDTO getOrder(Long id) throws NotFoundException, NotAuthorizedException;

    OrderEntity getOrderEntity(Long id) throws NotFoundException;

    List<OrderEntityDTO> getAllOrders() throws NotFoundException, NotAuthorizedException;

    OrderEntityDTO createOrder(OrderEntityDTO newOrderEntity) throws NotFoundException, NotValidArgumentException;

    OrderEntityDTO updateOrder(Long id, OrderEntityDTO updatedOrderEntity) throws NotFoundException, NotAuthorizedException;

    void deleteOrder(Long id) throws NotFoundException, NotAuthorizedException;
}
