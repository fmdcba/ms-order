package com.mindhub.ms_order.controllers;

import com.mindhub.ms_order.dtos.OrderItemDTO;
import com.mindhub.ms_order.models.OrderItem;
import com.mindhub.ms_order.services.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    @Autowired
    OrderItemService orderItemService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderItem(@PathVariable long id) throws Exception {
        OrderItemDTO order = orderItemService.getOrderItem(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrderItems() {
        List<OrderItemDTO> orders = orderItemService.getAllOrderItems();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createOrderItem(@RequestBody OrderItemDTO newOrderItem) throws Exception {
        OrderItem newOrderItemEntity = orderItemService.createOrderItem(newOrderItem);
        return new ResponseEntity<>(newOrderItemEntity, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderItem(@PathVariable Long id, @RequestBody OrderItemDTO updatedOrder) throws Exception {
        OrderItem updatedOrderToEntity = orderItemService.updateOrderItem(id, updatedOrder);
        return new ResponseEntity<>(updatedOrderToEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
        return new ResponseEntity<>("Order deleted successfully.", HttpStatus.OK);
    }
}
