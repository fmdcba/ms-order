package com.mindhub.ms_order.mappers;

import com.mindhub.ms_order.dtos.OrderDTO;
import com.mindhub.ms_order.models.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderDTO orderToDTO (Order order) {
        return new OrderDTO(order);
    }

    public List<OrderDTO> orderListToDTO(List<Order> orders) {
        return orders.stream().map(order -> new OrderDTO(order)).toList();
    }

    public Order orderToEntity (OrderDTO order){
        return new Order (order.getUserId(), order.getProducts(), order.getStatus());
    }

    public Order updateOrderToEntity (Order orderToUpdate, OrderDTO updatedOrder) {
        orderToUpdate.setUserId(updatedOrder.getUserId());
        orderToUpdate.setProducts(updatedOrder.getProducts());
        orderToUpdate.setStatus(updatedOrder.getStatus());

        return orderToUpdate;
    }
}
