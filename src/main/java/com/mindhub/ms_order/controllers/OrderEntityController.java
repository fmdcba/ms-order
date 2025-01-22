package com.mindhub.ms_order.controllers;

import com.mindhub.ms_order.dtos.OrderEntityDTO;
import com.mindhub.ms_order.models.OrderEntity;
import com.mindhub.ms_order.services.OrderEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderEntityController {

    @Autowired
    private OrderEntityService orderEntityService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable long id) throws Exception {
        OrderEntityDTO order = orderEntityService.getOrder(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        List<OrderEntityDTO> orders = orderEntityService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderEntityDTO newOrder) {
        OrderEntity newOrderEntity = orderEntityService.createOrder(newOrder);
        return new ResponseEntity<>(newOrderEntity, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderEntityDTO updatedOrder) throws Exception {
        OrderEntity updatedOrderToEntity = orderEntityService.updateOrder(id, updatedOrder);
        return new ResponseEntity<>(updatedOrderToEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        orderEntityService.deleteOrder(id);
        return new ResponseEntity<>("Order deleted successfully.", HttpStatus.OK);
    }
}
