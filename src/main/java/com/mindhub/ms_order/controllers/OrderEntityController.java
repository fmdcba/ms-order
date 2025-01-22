package com.mindhub.ms_order.controllers;

import com.mindhub.ms_order.dtos.OrderEntityDTO;
import com.mindhub.ms_order.mappers.OrderEntityMapper;
import com.mindhub.ms_order.models.OrderEntity;
import com.mindhub.ms_order.repositories.OrderEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderEntityController {

    @Autowired
    private OrderEntityMapper orderEntityMapper;

    @Autowired
    private OrderEntityRepository orderEntityRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable long id) throws Exception {
        OrderEntityDTO order = orderEntityMapper.orderToDTO(orderEntityRepository.findById(id).orElseThrow(() -> new Exception("Not Found.")));
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        List<OrderEntityDTO> orders = orderEntityMapper.orderListToDTO(orderEntityRepository.findAll());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderEntityDTO newOrder) {
        OrderEntity newOrderEntity = orderEntityRepository.save(orderEntityMapper.orderToEntity(newOrder));
        return new ResponseEntity<>(newOrderEntity, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderEntityDTO updatedOrder) throws Exception {
        //validate ID
        //validate entries for put
        OrderEntity orderToUpdate = orderEntityRepository.findById(id).orElseThrow(() -> new Exception("Not Found."));
        OrderEntity updatedOrderToEntity = orderEntityRepository.save(orderEntityMapper.updateOrderToEntity(orderToUpdate, updatedOrder));
        return new ResponseEntity<>(updatedOrderToEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        orderEntityRepository.deleteById(id);
        return new ResponseEntity<>("Order deleted successfully.", HttpStatus.OK);
    }
}
