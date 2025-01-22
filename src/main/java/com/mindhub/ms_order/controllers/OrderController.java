package com.mindhub.ms_order.controllers;

import com.mindhub.ms_order.dtos.OrderDTO;
import com.mindhub.ms_order.mappers.OrderMapper;
import com.mindhub.ms_order.models.Order;
import com.mindhub.ms_order.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Orders")
public class OrderController {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable long id) throws Exception {
        OrderDTO order = orderMapper.orderToDTO(orderRepository.findById(id).orElseThrow(() -> new Exception("Not Found.")));
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        List<OrderDTO> orders = orderMapper.orderListToDTO(orderRepository.findAll());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO newOrder) {
        Order newOrderEntity = orderRepository.save(orderMapper.orderToEntity(newOrder));
        return new ResponseEntity<>(newOrderEntity, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderDTO updatedOrder) throws Exception {
        //validate ID
        //validate entries for put
        Order orderToUpdate = orderRepository.findById(id).orElseThrow(() -> new Exception("Not Found."));
        Order updatedOrderToEntity = orderMapper.updateOrderToEntity(orderToUpdate, updatedOrder);
        return new ResponseEntity<>(updatedOrderToEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return new ResponseEntity<>("Order deleted successfully.", HttpStatus.OK);
    }
}
