package com.mindhub.ms_order.controllers;

import com.mindhub.ms_order.dtos.OrderItemDTO;
import com.mindhub.ms_order.exceptions.NotFoundException;
import com.mindhub.ms_order.exceptions.NotValidArgument;
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

    @Autowired
    ControllerValidations controllerValidations;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderItem(@PathVariable long id) throws NotFoundException, NotValidArgument {
        controllerValidations.isValidId(id);
        OrderItemDTO order = orderItemService.getOrderItem(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrderItems() {
        List<OrderItemDTO> orders = orderItemService.getAllOrderItems();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createOrderItem(@RequestBody OrderItemDTO newOrderItem) throws NotFoundException, NotValidArgument {
        validateEntries(newOrderItem);
        OrderItem newOrderItemEntity = orderItemService.createOrderItem(newOrderItem);
        return new ResponseEntity<>(newOrderItemEntity, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderItem(@PathVariable Long id, @RequestBody OrderItemDTO updatedOrder) throws NotFoundException, NotValidArgument {
        validateEntries(updatedOrder);
        OrderItem updatedOrderToEntity = orderItemService.updateOrderItem(id, updatedOrder);
        return new ResponseEntity<>(updatedOrderToEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable Long id) throws NotValidArgument {
        controllerValidations.isValidId(id);
        orderItemService.deleteOrderItem(id);
        return new ResponseEntity<>("Order deleted successfully.", HttpStatus.OK);
    }

    public void isQuantityMoreThanZero (Integer quantity) throws NotValidArgument {
        if (quantity <= 0) {
            throw new NotValidArgument("Quantity hast to be at least 1.");
        }
    }

    public void validateEntries(OrderItemDTO orderItem) throws NotValidArgument {
        controllerValidations.isValidId(orderItem.getOrderId());
        controllerValidations.isValidId(orderItem.getProductId());
        isQuantityMoreThanZero(orderItem.getQuantity());
    }
}
