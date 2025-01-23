package com.mindhub.ms_order.controllers;

import com.mindhub.ms_order.dtos.OrderEntityDTO;
import com.mindhub.ms_order.dtos.OrderItemDTO;
import com.mindhub.ms_order.exceptions.NotFoundException;
import com.mindhub.ms_order.exceptions.NotValidArgumentException;
import com.mindhub.ms_order.models.OrderEntity;
import com.mindhub.ms_order.models.OrderStatus;
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

    @Autowired
    private ControllerValidations controllerValidations;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable long id) throws NotFoundException, NotValidArgumentException {
        controllerValidations.isValidId(id);
        OrderEntityDTO order = orderEntityService.getOrder(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        List<OrderEntityDTO> orders = orderEntityService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderEntityDTO newOrder) throws NotValidArgumentException {
        validateEntries(newOrder);
        OrderEntity newOrderEntity = orderEntityService.createOrder(newOrder);
        return new ResponseEntity<>(newOrderEntity, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderEntityDTO updatedOrder) throws NotFoundException, NotValidArgumentException {
        validateEntries(updatedOrder);
        OrderEntity updatedOrderToEntity = orderEntityService.updateOrder(id, updatedOrder);
        return new ResponseEntity<>(updatedOrderToEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) throws NotValidArgumentException, NotFoundException {
        controllerValidations.isValidId(id);
        orderEntityService.deleteOrder(id);
        return new ResponseEntity<>("Order deleted successfully.", HttpStatus.OK);
    }


    public void isValidStatus(OrderStatus status) throws NotValidArgumentException {
        if (status.equals(OrderStatus.COMPLETED) || status.equals(OrderStatus.PENDING)) {
        } else {
            throw new NotValidArgumentException("Status has to be COMPLETED or PENDING.");
        }
    }

    public void validateEntries(OrderEntityDTO order) throws NotValidArgumentException {
        controllerValidations.isValidId(order.getUserId());
        isValidStatus(order.getStatus());
    }
}
