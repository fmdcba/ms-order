package com.mindhub.ms_order.services.Impl;

import com.mindhub.ms_order.dtos.OrderEntityDTO;
import com.mindhub.ms_order.mappers.OrderEntityMapper;
import com.mindhub.ms_order.models.OrderEntity;
import com.mindhub.ms_order.repositories.OrderEntityRepository;
import com.mindhub.ms_order.services.OrderEntityService;
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
    public OrderEntityDTO getOrder(Long id) throws Exception {
        return orderEntityMapper.orderToDTO(findById(id));
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
    public OrderEntity updateOrder(Long id, OrderEntityDTO updatedOrder) throws Exception {
        OrderEntity orderToUpdate = findById(id);
        return save(orderEntityMapper.updateOrderToEntity(orderToUpdate, updatedOrder));
    }

    @Override
    public void deleteOrder(Long id) {
        deleteById(id);
    }

    @Override
    public OrderEntity findById(Long id) throws Exception {
        return (orderEntityRepository.findById(id).orElseThrow(() -> new Exception("Not Found.")));
    }

    @Override
    public List<OrderEntity> findAll() {
        return orderEntityRepository.findAll();
    }

    @Override
    public void deleteById(long id) {
        orderEntityRepository.deleteById(id);
    }

    @Override
    public OrderEntity save(OrderEntity order) {
        return orderEntityRepository.save(order);
    }
}
