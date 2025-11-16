package com.kadri.order.service;

import com.kadri.order.dto.OrderItemRequest;
import com.kadri.order.dto.OrderRequest;
import com.kadri.order.model.Order;
import com.kadri.order.model.OrderItem;
import com.kadri.order.model.OrderStatus;
import com.kadri.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {


    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrder(OrderRequest request) {
        if(request.getItems() == null || request.getItems().isEmpty()){
            throw new IllegalArgumentException("Order must contain at least 1 item !");
        }

        Order order = new Order();
        order.setUserId(request.getUserId());

        BigDecimal total = BigDecimal.ZERO;

        List<OrderItem> items = new ArrayList<>();
        for(OrderItemRequest itemRequest : request.getItems()){
            OrderItem item = OrderItem.builder()
                    .order(order)
                    .productId(itemRequest.getProductId())
                    .quantity(itemRequest.getQuantity())
                    .price(itemRequest.getPrice())
                    .build();
            BigDecimal line = itemRequest.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            total = total.add(line);
            items.add(item);
        }

        order.setTotalAmount(total);
        order.setItems(items);
        return orderRepository.save(order);
    }

    public Order getById(Long id){
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + id));
    }

    public List<Order> getByUserId(Long userId){
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getAll(){
        return orderRepository.findAll();
    }

    public Order updateStatus(Long id, OrderStatus status){
        Order order = getById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void delete(Long id){
        orderRepository.deleteById(id);
    }
}
