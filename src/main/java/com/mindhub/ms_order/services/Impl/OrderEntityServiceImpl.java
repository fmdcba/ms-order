package com.mindhub.ms_order.services.Impl;

import com.mindhub.ms_order.dtos.OrderEntityDTO;
import com.mindhub.ms_order.exceptions.NotFoundException;
import com.mindhub.ms_order.mappers.OrderEntityMapper;
import com.mindhub.ms_order.models.OrderEntity;
import com.mindhub.ms_order.repositories.OrderEntityRepository;
import com.mindhub.ms_order.services.OrderEntityService;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderEntityServiceImpl implements OrderEntityService {

    @Autowired
    OrderEntityRepository orderEntityRepository;

    @Autowired
    OrderEntityMapper orderEntityMapper;

    @Override
    public OrderEntityDTO getOrder(Long id) throws NotFoundException {
        if(existsById(id)) {
            return orderEntityMapper.orderToDTO(findById(id));
        } else {
            throw new NotFoundException("Not found.");
        }
    }

    @Override
    public OrderEntity getOrderEntity(Long id) throws NotFoundException {
        if(existsById(id)) {
            return findById(id);
        } else {
            throw new NotFoundException("Not found.");
        }
    }

    @Override
    public List<OrderEntityDTO> getAllOrders() {
        return  orderEntityMapper.orderListToDTO(orderEntityRepository.findAll());
    }

    @Override
    public OrderEntity createOrder(OrderEntityDTO newOrder) {
        return save(orderEntityMapper.orderToEntity(newOrder));
    }

    @Override
    public OrderEntity updateOrder(Long id, OrderEntityDTO updatedOrder) throws NotFoundException {
        if(existsById(id)) {
            OrderEntity orderToUpdate = findById(id);
            return save(orderEntityMapper.updateOrderToEntity(orderToUpdate, updatedOrder));
        } else {
            throw new NotFoundException("Not found.");
        }
    }

    @Override
    public void deleteOrder(Long id) throws NotFoundException {
        if(existsById(id)) {
            deleteById(id);
        } else {
            throw new NotFoundException("Not found.");
        }
    }

    @Override
    public OrderEntity findById(Long id) throws NotFoundException {
        return (orderEntityRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found.")));
    }

    @Override
    public List<OrderEntity> findAll() {
        return orderEntityRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        orderEntityRepository.deleteById(id);
    }

    @Override
    public OrderEntity save(OrderEntity order) {
        return orderEntityRepository.save(order);
    }

    @Override
    public Boolean existsById(Long id) {
        return orderEntityRepository.existsById(id);
    }
}
