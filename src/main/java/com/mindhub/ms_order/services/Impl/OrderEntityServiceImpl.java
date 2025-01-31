package com.mindhub.ms_order.services.Impl;

import com.mindhub.ms_order.config.JwtUtils;
import com.mindhub.ms_order.config.RabbitMQConfig;
import com.mindhub.ms_order.dtos.OrderEntityDTO;
import com.mindhub.ms_order.exceptions.NotAuthorizedException;
import com.mindhub.ms_order.exceptions.NotFoundException;
import com.mindhub.ms_order.exceptions.NotValidArgumentException;
import com.mindhub.ms_order.mappers.OrderEntityMapper;
import com.mindhub.ms_order.models.OrderEntity;
import com.mindhub.ms_order.models.OrderStatus;
import com.mindhub.ms_order.repositories.OrderEntityRepository;
import com.mindhub.ms_order.services.OrderEntityService;
import com.mindhub.ms_order.utils.ServiceValidations;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OrderEntityServiceImpl implements OrderEntityService {

    private static final Logger log = LoggerFactory.getLogger(OrderEntityServiceImpl.class);

    @Autowired
    private OrderEntityRepository orderEntityRepository;

    @Autowired
    private OrderEntityMapper orderEntityMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ServiceValidations serviceValidations;

    @Override
    public OrderEntityDTO getOrder(Long id) throws NotFoundException, NotAuthorizedException {
        try {
            OrderEntityDTO order = orderEntityMapper.orderToDTO(findById(id));
            validateAuthorization(order.getUserId());
            return order;
        } catch (NotFoundException | NotAuthorizedException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public OrderEntity getOrderEntity(Long id) throws NotFoundException {
        try {
            return findById(id);
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public List<OrderEntityDTO> getAllOrders() throws NotFoundException, NotAuthorizedException {
        try {
            validateIsAdmin();
            List<OrderEntityDTO> ordersList = orderEntityMapper.orderListToDTO(orderEntityRepository.findAll());
            validateOrderListIsEmpty(ordersList);
            return ordersList;
        } catch (NotFoundException | NotAuthorizedException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public OrderEntityDTO createOrder(OrderEntityDTO newOrder) throws NotFoundException, NotValidArgumentException {
        try {
            if (!serviceValidations.isAdmin()) {
                Long userId = serviceValidations.getUserId();
                hasPendingOrder(userId);
                newOrder.setUserId(userId);
            }

            isValidUserId(newOrder.getUserId());
            OrderEntity savedOrder = save(orderEntityMapper.orderToEntity(newOrder));
            OrderEntityDTO order = orderEntityMapper.orderToDTO(savedOrder);

            amqpTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.ORDER_CREATED_ROUTING_KEY,
                    order
            );

            return order;
        } catch (NotFoundException | NotValidArgumentException e) {
            log.warn(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e;
        }
    }

    @Override
    @Transactional
    public OrderEntityDTO updateOrder(Long id, OrderEntityDTO updatedOrder) throws NotFoundException, NotAuthorizedException {
        try {
            validateAuthorization(updatedOrder.getUserId());
            isValidUserId(updatedOrder.getUserId());
            OrderEntity orderToUpdate = findById(id);
            OrderEntity savedOrder = save(orderEntityMapper.updateOrderToEntity(orderToUpdate, updatedOrder));
            return orderEntityMapper.orderToDTO(savedOrder);
        } catch (NotFoundException | NotAuthorizedException e) {
            log.warn(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) throws NotFoundException, NotAuthorizedException {
        try {
            validateAuthorization(findById(id).getUserId());
            existsById(id);
            deleteById(id);
        } catch (NotFoundException | NotAuthorizedException e) {
            log.warn(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e;
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

    private void validateOrderListIsEmpty(List<OrderEntityDTO> allOrders) throws NotFoundException {
        if (allOrders.toArray().length == 0) {
            throw new NotFoundException("No orders found to show.");
        }
    }

    private void isValidUserId(Long id) throws NotFoundException {
        String url = "http://localhost:8080/api/users/" + id;
        try {
            HttpEntity<String> entity = createAuthHttpEntity(null);
            restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException("User with ID: " + id + " not found.");
        }
    }

    @Override
    public <T> HttpEntity<T> createAuthHttpEntity(T body){
        String token = jwtUtils.getJwtToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        if (body != null) {
            return new HttpEntity<>(body, headers);
        } else {
            return new HttpEntity<>(headers);
        }
    }

    private void validateAuthorization(Long id) throws NotAuthorizedException {
        String token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);

        if (!userId.equals(id)) {
            validateIsAdmin();
        }
    }

    private void hasPendingOrder (Long id) throws NotValidArgumentException {
        boolean hasPendingOrder = orderEntityRepository.existsByUserIdAndStatus(id, OrderStatus.PENDING);

        if (hasPendingOrder) {
            throw new NotValidArgumentException("You already have a pending order. Please complete it before creating a new one.");
        }
    }

    @Override
    public void validateIsAdmin() throws NotAuthorizedException {
        String token = jwtUtils.getJwtToken();
        String userRole = jwtUtils.extractRole(token);

        if (!userRole.equals("ADMIN")) {
            throw new NotAuthorizedException("You are not authorized to access this resource.");
        }
    }
}



