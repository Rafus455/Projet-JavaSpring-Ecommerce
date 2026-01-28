package com.b2.e_commerce.controller.REST;

import com.b2.e_commerce.dto.OrderItemRequestDTO;
import com.b2.e_commerce.entity.OrderItem;
import com.b2.e_commerce.service.OrderItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    // CREATE
    @PostMapping
    public OrderItem addItemToOrder(@Valid @RequestBody OrderItemRequestDTO dto) {
        return orderItemService.addItemToOrder(dto);
    }

    // UPDATE
    @PutMapping("/{itemId}")
    public void updateItemQuantity(
            @PathVariable Long itemId,
            @RequestParam int quantity
    ) {
    	System.out.println("euhh");
        orderItemService.updateItemQuantity(itemId, quantity);
    }

    // DELETE
    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Long itemId) {
        orderItemService.deleteItem(itemId);
    }
}
