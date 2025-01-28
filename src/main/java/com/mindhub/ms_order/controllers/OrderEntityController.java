package com.mindhub.ms_order.controllers;

import com.mindhub.ms_order.dtos.OrderEntityDTO;
import com.mindhub.ms_order.exceptions.NotFoundException;
import com.mindhub.ms_order.exceptions.NotValidArgumentException;
import com.mindhub.ms_order.models.OrderEntity;
import com.mindhub.ms_order.models.OrderStatus;
import com.mindhub.ms_order.services.Impl.PDFGeneratorServiceImpl;
import com.mindhub.ms_order.services.OrderEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderEntityController {

    @Autowired
    private OrderEntityService orderEntityService;

    @Autowired
    private ControllerValidations controllerValidations;

    @Autowired
    PDFGeneratorServiceImpl pdfGeneratorService;

    @GetMapping("/{id}")
    @Operation(summary = "Get Order by id", description = "Return order if ID is valid and exists in DB")
        @ApiResponse(responseCode = "200", description = "Return order, and http code status OK")
        @ApiResponse(responseCode = "400", description = "Error msg Bad request: Invalid ID")
        @ApiResponse(responseCode = "404", description = "Error msg: Not found")
    public ResponseEntity<?> getOrder(@PathVariable long id) throws NotFoundException, NotValidArgumentException {
        controllerValidations.isValidId(id);
        OrderEntityDTO order = orderEntityService.getOrder(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Return all orders in DB")
        @ApiResponse(responseCode = "200", description = "Return list of orders, and http code status OK")
    public ResponseEntity<?> getAllOrders() {
        List<OrderEntityDTO> orders = orderEntityService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create order", description = "Return order if ID is valid and exists in DB")
        @ApiResponse(responseCode = "200", description = "Return created order, and http code status OK")
        @ApiResponse(responseCode = "400", description = "Error msg Bad request: Invalid ID")
        @ApiResponse(responseCode = "404", description = "Error msg: Not found")
    public ResponseEntity<byte[]> createOrder(@RequestBody OrderEntityDTO newOrder) throws NotFoundException, NotValidArgumentException {
        validateEntries(newOrder);

        OrderEntity createdOrder = orderEntityService.createOrder(newOrder);

        ByteArrayInputStream pdfStream = pdfGeneratorService.export(createdOrder);
        byte[] pdfBytes = pdfStream.readAllBytes();

        String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String filename = "order_" + createdOrder.getId() + "_" + date + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(filename).build());
        headers.setContentLength(pdfBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order", description = "Return updated order if ID is valid and exists in DB")
        @ApiResponse(responseCode = "200", description = "Return updated order, and http code status OK")
        @ApiResponse(responseCode = "400", description = "Error msg Bad request: Invalid ID")
        @ApiResponse(responseCode = "404", description = "Error msg: Not found")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderEntityDTO updatedOrder) throws NotFoundException, NotValidArgumentException {
        validateEntries(updatedOrder);
        OrderEntity updatedOrderToEntity = orderEntityService.updateOrder(id, updatedOrder);
        return new ResponseEntity<>(updatedOrderToEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order", description = "Return msg of operation completed successfully")
        @ApiResponse(responseCode = "200", description = "Return msg: Order deleted successfully, and http code status OK")
        @ApiResponse(responseCode = "400", description = "Error msg Bad request: Invalid ID")
        @ApiResponse(responseCode = "404", description = "Error msg: Not found")
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
