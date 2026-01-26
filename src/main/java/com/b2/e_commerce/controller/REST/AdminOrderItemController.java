package com.b2.e_commerce.controller.REST;

import com.b2.e_commerce.dto.OrderItemRequestDTO;
import com.b2.e_commerce.service.OrderItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/order-items")
public class AdminOrderItemController {

    private final OrderItemService orderItemService;

    public AdminOrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addItemToOrder(@Valid @RequestBody OrderItemRequestDTO dto) {
        orderItemService.addItemToOrder(dto);
    }

    // UPDATE
    @PutMapping("/{itemId}")
    public void updateItemQuantity(
            @PathVariable Long itemId,
            @RequestParam int quantity
    ) {
        orderItemService.updateItemQuantity(itemId, quantity);
    }

    // DELETE
    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Long itemId) {
        orderItemService.deleteItem(itemId);
    }
}
