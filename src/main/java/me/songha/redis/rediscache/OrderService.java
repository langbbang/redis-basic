package me.songha.redis.rediscache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Cacheable(value = "Order", key = "#orderId", cacheManager = "cacheManager")
    public Order getOrder(Integer orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order Not Found"));
    }

    @CachePut(value = "Order", key = "#orderId", cacheManager = "cacheManager")
    public Order updateOrderStatus(Order order, Integer orderId) {
        Order orderObject = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order Not Found"));
        switch (orderObject.getOrderStatus()) {
            case ready -> orderObject.setOrderStatus(OrderStatus.processing);
            case processing -> orderObject.setOrderStatus(OrderStatus.shipped);
            case shipped -> orderObject.setOrderStatus(OrderStatus.delivered);
            default -> throw new OrderStatusException("Order Status cannot change");
        }

        return orderRepository.save(orderObject);
    }

    @CacheEvict(value = "Order", key = "#orderId", cacheManager = "cacheManager")
    public void deleteOrder(Integer orderId) {
        Order orderObject = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order Not Found"));
        orderRepository.delete(orderObject);
    }

    @Cacheable(value = "Order", cacheManager = "cacheManager")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}