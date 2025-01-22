package com.mindhub.ms_order.mappers;

import com.mindhub.ms_order.dtos.OrderEntityDTO;
import com.mindhub.ms_order.models.OrderEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderEntityMapper {

    public OrderEntityDTO orderToDTO (OrderEntity order) {
        return new OrderEntityDTO(order);
    }

    public List<OrderEntityDTO> orderListToDTO(List<OrderEntity> orders) {
        return orders.stream().map(order -> new OrderEntityDTO(order)).toList();
    }

    public OrderEntity orderToEntity (OrderEntityDTO order){
        return new OrderEntity(order.getUserId(), order.getStatus());
    }

    public OrderEntity updateOrderToEntity (OrderEntity orderToUpdate, OrderEntityDTO updatedOrder) {
        orderToUpdate.setUserId(updatedOrder.getUserId());
        orderToUpdate.setStatus(updatedOrder.getStatus());

        return orderToUpdate;
    }
}
