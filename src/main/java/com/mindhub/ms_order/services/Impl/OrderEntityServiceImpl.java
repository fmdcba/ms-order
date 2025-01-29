package com.mindhub.ms_order.services.Impl;

import com.mindhub.ms_order.config.RabbitMQConfig;
import com.mindhub.ms_order.dtos.OrderEntityDTO;
import com.mindhub.ms_order.exceptions.NotFoundException;
import com.mindhub.ms_order.exceptions.NotValidArgumentException;
import com.mindhub.ms_order.mappers.OrderEntityMapper;
import com.mindhub.ms_order.models.OrderEntity;
import com.mindhub.ms_order.repositories.OrderEntityRepository;
import com.mindhub.ms_order.services.OrderEntityService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OrderEntityServiceImpl implements OrderEntityService {

    @Autowired
    OrderEntityRepository orderEntityRepository;

    @Autowired
    OrderEntityMapper orderEntityMapper;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public OrderEntityDTO getOrder(Long id) throws NotFoundException {
        try {
            return orderEntityMapper.orderToDTO(findById(id));
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public OrderEntity getOrderEntity(Long id) throws NotFoundException {
        try {
            return findById(id);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public List<OrderEntityDTO> getAllOrders() {
        return  orderEntityMapper.orderListToDTO(orderEntityRepository.findAll());
    }

    @Override
    public OrderEntity createOrder(OrderEntityDTO newOrder) throws NotFoundException {
        try {
            isValidUserId(newOrder.getUserId());
            OrderEntity createdOrder = orderEntityMapper.orderToEntity(newOrder);
            save(createdOrder);


            OrderEntityDTO createdOrderDTO = orderEntityMapper.orderToDTO(createdOrder);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_CREATED_ROUTING_KEY,
                    createdOrderDTO
            );

            return createdOrder;
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public OrderEntity updateOrder(Long id, OrderEntityDTO updatedOrder) throws NotFoundException {
        try {
            OrderEntity orderToUpdate = findById(id);
            return save(orderEntityMapper.updateOrderToEntity(orderToUpdate, updatedOrder));
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public void deleteOrder(Long id) throws NotFoundException {
        try {
            existsById(id);
            deleteById(id);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public OrderEntity findById(Long id) throws NotFoundException {
        return (orderEntityRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found.")));
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
    public void existsById(Long id) throws NotFoundException {
        if (!orderEntityRepository.existsById(id)) {
            throw new NotFoundException("Order not found.");
        }
    }

    @Override
    public void isValidUserId(Long id) throws NotFoundException {
        String url = "http://localhost:8081/api/users/" + id;
        try {
            restTemplate.exchange(url, HttpMethod.GET, null, Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException("User with ID: " + id + " not found.");
        }
    }
}



