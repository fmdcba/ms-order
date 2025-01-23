package com.mindhub.ms_order.services.Impl;

import com.mindhub.ms_order.dtos.OrderItemDTO;
import com.mindhub.ms_order.exceptions.NotFoundException;
import com.mindhub.ms_order.mappers.OrderItemMapper;
import com.mindhub.ms_order.models.OrderEntity;
import com.mindhub.ms_order.models.OrderItem;
import com.mindhub.ms_order.repositories.OrderItemRepository;
import com.mindhub.ms_order.services.OrderEntityService;
import com.mindhub.ms_order.services.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    OrderEntityService orderEntityService;

    @Override
    public OrderItemDTO getOrderItem(Long id) throws NotFoundException {
        if(existsById(id)) {
            return orderItemMapper.orderItemToDTO(findById(id));
        } else {
            throw new NotFoundException("Not found.");
        }
    }

    @Override
    public List<OrderItemDTO> getAllOrderItems() {
        return  orderItemMapper.orderItemListToDTO(orderItemRepository.findAll());
    }

    @Override
    public OrderItem createOrderItem(OrderItemDTO newOrderItem) throws NotFoundException {
        OrderEntity orderEntity = orderEntityService.getOrderEntity(newOrderItem.getOrderId());
        return save(orderItemMapper.orderItemToEntity(orderEntity, newOrderItem));
    }

    @Override
    public OrderItem updateOrderItem(Long id, OrderItemDTO updatedOrderItem) throws NotFoundException {
        if(existsById(id)) {
            OrderItem orderItemToUpdate = findById(id);
            return save(orderItemMapper.updateOrderItemToEntity(orderItemToUpdate, updatedOrderItem));
        } else {
            throw new NotFoundException("Not found.");
        }
    }

    @Override
    public void deleteOrderItem(Long id) throws NotFoundException {
        if(existsById(id)) {
            deleteById(id);
        } else {
            throw new NotFoundException("Not found.");
        }
    }

    @Override
    public OrderItem findById(Long id) throws NotFoundException {
        return (orderItemRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found.")));
    }

    @Override
    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        orderItemRepository.deleteById(id);
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }


    @Override
    public Boolean existsById(Long id) {
        return orderItemRepository.existsById(id);
    }
}
