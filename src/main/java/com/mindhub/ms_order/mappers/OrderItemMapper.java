package com.mindhub.ms_order.mappers;

import com.mindhub.ms_order.dtos.OrderItemDTO;
import com.mindhub.ms_order.models.OrderEntity;
import com.mindhub.ms_order.models.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderItemMapper {

    public OrderItemDTO orderItemToDTO (OrderItem orderItem) {
        return new OrderItemDTO(orderItem);
    }

    public List<OrderItemDTO> orderItemListToDTO(List<OrderItem> orderItems) {
        return orderItems.stream().map(orderItem -> new OrderItemDTO(orderItem)).toList();
    }

    public OrderItem orderItemToEntity (OrderEntity order, OrderItemDTO orderItem){
        return new OrderItem (order, orderItem.getProductId(), orderItem.getQuantity());
    }

    public OrderItem updateOrderItemToEntity (OrderItem orderItemToUpdate, OrderItemDTO updatedOrderItem) {
        orderItemToUpdate.setProductId(updatedOrderItem.getProductId());
        orderItemToUpdate.setQuantity(updatedOrderItem.getQuantity());

        return orderItemToUpdate;
    }
}
