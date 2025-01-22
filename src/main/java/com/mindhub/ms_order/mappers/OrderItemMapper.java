package com.mindhub.ms_order.mappers;

import com.mindhub.ms_order.dtos.OrderItemDTO;
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

    public OrderItem orderItemToEntity (OrderItemDTO orderItem){
        return new OrderItem (orderItem.getOrderId(), orderItem.getProductId(), orderItem.getQuantity());
    }

    public OrderItem updateOrderItemToEntity (OrderItem orderItemToUpdate, OrderItemDTO updatedOrderItem) {
        orderItemToUpdate.setOrderId(updatedOrderItem.getOrderId());
        orderItemToUpdate.setProductId(updatedOrderItem.getProductId());
        orderItemToUpdate.setQuantity(updatedOrderItem.getQuantity());

        return orderItemToUpdate;
    }
}
