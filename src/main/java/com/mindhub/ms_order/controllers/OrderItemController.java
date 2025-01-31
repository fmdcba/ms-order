package com.mindhub.ms_order.controllers;

import com.mindhub.ms_order.dtos.OrderItemDTO;
import com.mindhub.ms_order.exceptions.NotAuthorizedException;
import com.mindhub.ms_order.exceptions.NotFoundException;
import com.mindhub.ms_order.exceptions.NotValidArgumentException;
import com.mindhub.ms_order.services.OrderItemService;
import com.mindhub.ms_order.utils.ControllerValidations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ControllerValidations controllerValidations;

    @GetMapping("/{id}")
    @Operation(summary = "Get Order Item by id", description = "Return order item if ID is valid and exists in DB")
        @ApiResponse(responseCode = "200", description = "Return order item, and http code status OK")
        @ApiResponse(responseCode = "400", description = "Error msg Bad request: Invalid ID")
        @ApiResponse(responseCode = "404", description = "Error msg: Not found")
    public ResponseEntity<?> getOrderItem(@PathVariable long id) throws NotFoundException, NotValidArgumentException, NotAuthorizedException {
        controllerValidations.isValidId(id);
        OrderItemDTO order = orderItemService.getOrderItem(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all order items", description = "Return all order items in DB")
        @ApiResponse(responseCode = "200", description = "Return list of order items, and http code status OK")
        @ApiResponse(responseCode = "404", description = "Error msg: No order items found to show")
    public ResponseEntity<?> getAllOrderItems() throws NotFoundException, NotAuthorizedException {
        List<OrderItemDTO> orders = orderItemService.getAllOrderItems();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create order item", description = "Return order item if ID is valid and exists in DB")
        @ApiResponse(responseCode = "200", description = "Return created order item, and http code status OK")
        @ApiResponse(responseCode = "400", description = "Error msg Bad request: Invalid ID")
        @ApiResponse(responseCode = "404", description = "Error msg: Not found")
    public ResponseEntity<?> createOrderItem(@RequestBody OrderItemDTO newOrderItem) throws NotFoundException, NotValidArgumentException {
        validateEntries(newOrderItem);
        OrderItemDTO orderItem = orderItemService.createOrderItem(newOrderItem);
        return new ResponseEntity<>(orderItem, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order item", description = "Return updated order item if ID is valid and exists in DB")
        @ApiResponse(responseCode = "200", description = "Return updated order item, and http code status OK")
        @ApiResponse(responseCode = "400", description = "Error msg Bad request: Invalid ID")
        @ApiResponse(responseCode = "404", description = "Error msg: Not found")
    public ResponseEntity<?> updateOrderItem(@PathVariable Long id, @RequestBody OrderItemDTO updatedOrder) throws NotFoundException, NotValidArgumentException, NotAuthorizedException {
        validateEntries(updatedOrder);
        OrderItemDTO orderItem = orderItemService.updateOrderItem(id, updatedOrder);
        return new ResponseEntity<>(orderItem, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order item", description = "Return msg of operation completed successfully")
        @ApiResponse(responseCode = "200", description = "Return msg: Order item deleted successfully, and http code status OK")
        @ApiResponse(responseCode = "400", description = "Error msg Bad request: Invalid ID")
        @ApiResponse(responseCode = "404", description = "Error msg: Not found")
    public ResponseEntity<?> deleteOrderItem(@PathVariable Long id) throws NotValidArgumentException, NotFoundException, NotAuthorizedException {
        controllerValidations.isValidId(id);
        orderItemService.deleteOrderItem(id);
        return new ResponseEntity<>("Order item deleted successfully.", HttpStatus.OK);
    }

    public void isQuantityMoreThanZero (Integer quantity) throws NotValidArgumentException {
        if (quantity <= 0) {
            throw new NotValidArgumentException("Quantity has to be at least 1.");
        }
    }

    public void validateEntries(OrderItemDTO orderItem) throws NotValidArgumentException {
        controllerValidations.isValidId(orderItem.getOrderId());
        controllerValidations.isValidId(orderItem.getProductId());
        isQuantityMoreThanZero(orderItem.getQuantity());
    }
}
