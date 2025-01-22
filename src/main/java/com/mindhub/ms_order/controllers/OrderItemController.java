package com.mindhub.ms_order.controllers;

import com.mindhub.ms_order.dtos.OrderItemDTO;
import com.mindhub.ms_order.mappers.OrderItemMapper;
import com.mindhub.ms_order.models.OrderEntity;
import com.mindhub.ms_order.models.OrderItem;
import com.mindhub.ms_order.repositories.OrderEntityRepository;
import com.mindhub.ms_order.repositories.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderEntityRepository orderEntityRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderItem(@PathVariable long id) throws Exception {
        OrderItemDTO order = orderItemMapper.orderItemToDTO(orderItemRepository.findById(id).orElseThrow(() -> new Exception("Not Found.")));
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrderItems() {
        List<OrderItemDTO> orders = orderItemMapper.orderItemListToDTO(orderItemRepository.findAll());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createOrderItem(@RequestBody OrderItemDTO newOrderItem) throws Exception {
        OrderEntity orderEntity = orderEntityRepository.findById(newOrderItem.getOrderId()).orElseThrow(() -> new Exception("Not Found."));
        OrderItem newOrderItemEntity = orderItemRepository.save(orderItemMapper.orderItemToEntity(orderEntity, newOrderItem));
        return new ResponseEntity<>(newOrderItemEntity, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderItem(@PathVariable Long id, @RequestBody OrderItemDTO updatedOrder) throws Exception {
        //validate ID
        //validate entries for put
        OrderItem orderToUpdate = orderItemRepository.findById(id).orElseThrow(() -> new Exception("Not Found."));
        OrderItem updatedOrderToEntity = orderItemRepository.save(orderItemMapper.updateOrderItemToEntity(orderToUpdate, updatedOrder));
        return new ResponseEntity<>(updatedOrderToEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable Long id) {
        orderItemRepository.deleteById(id);
        return new ResponseEntity<>("Order deleted successfully.", HttpStatus.OK);
    }
}
