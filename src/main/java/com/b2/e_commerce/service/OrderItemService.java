package com.b2.e_commerce.service;

import com.b2.e_commerce.dto.OrderItemRequestDTO;
import com.b2.e_commerce.entity.CustomerOrder;
import com.b2.e_commerce.entity.OrderItem;
import com.b2.e_commerce.entity.Product;
import com.b2.e_commerce.entity.enums.OrderStatus;
import com.b2.e_commerce.exception.ResourceNotFoundException;
import com.b2.e_commerce.repository.OrderRepository;
import com.b2.e_commerce.repository.OrderItemRepository;
import com.b2.e_commerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderItemService(OrderItemRepository orderItemRepository,
                            OrderRepository orderRepository,
                            ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderItem addItemToOrder(OrderItemRequestDTO dto) {
        CustomerOrder order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order avec id " + dto.getOrderId() + " introuvable"));

        if (order.getStatus() == OrderStatus.PAID || order.getStatus() == OrderStatus.SHIPPED) {
            throw new IllegalStateException("Impossible de modifier une commande finalisée");
        }

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Produit avec id " + dto.getProductId() + " introuvable"));

        if (product.getStock() < dto.getQuantity()) {
            throw new IllegalStateException("Stock insuffisant");
        }

        OrderItem item = orderItemRepository
                .findByOrderIdAndProductId(order.getId(), product.getId())
                .orElse(null);

        double addedPrice = product.getPrice() * dto.getQuantity();

        double currentTotal = order.getPriceTotal();

        order.setPriceTotal(currentTotal + addedPrice);

        product.setStock(product.getStock() - dto.getQuantity());
        productRepository.save(product);

        if (item != null) {
            item.setQuantity(dto.getQuantity());
            orderItemRepository.save(item);
            return item;
        } else {
            OrderItem newItem = new OrderItem();
            newItem.setOrder(order);
            newItem.setProduct(product);
            newItem.setQuantity(dto.getQuantity());
            newItem.setUnitPrice(product.getPrice());

            orderItemRepository.save(newItem);
            return newItem;
        }
    }

    @Transactional
    public void updateItemQuantity(Long itemId, int newQuantity) {
    	System.out.println("début");
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Item avec id " + itemId + " introuvable"));

        CustomerOrder order = item.getOrder();
        if (order.getStatus() == OrderStatus.PAID || order.getStatus() == OrderStatus.SHIPPED) {
            throw new IllegalStateException("Impossible de modifier une commande finalisée");
        }

        Product product = item.getProduct();

        int oldQuantity = item.getQuantity();
        int diff = newQuantity - oldQuantity;

        if (diff > 0 && product.getStock() < diff) {
            throw new IllegalStateException("Stock insuffisant");
        }

        product.setStock(product.getStock() - diff);

        item.setQuantity(newQuantity);
    	System.out.println("euhh" + newQuantity);
    }

    public void deleteItem(Long itemId) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Item avec id " + itemId + " introuvable"));

        CustomerOrder order = item.getOrder();
        if (order.getStatus() == OrderStatus.PAID || order.getStatus() == OrderStatus.SHIPPED) {
            throw new IllegalStateException("Impossible de modifier une commande finalisée");
        }

        Product product = item.getProduct();

        product.setStock(product.getStock() + item.getQuantity());
        productRepository.save(product);

        orderItemRepository.delete(item);
    }
}
