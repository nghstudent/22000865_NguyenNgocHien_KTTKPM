package com.example.order_service.service;

import com.example.order_service.client.DownstreamClient;
import com.example.order_service.model.Food;
import com.example.order_service.model.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class OrderService {

    private final List<Order> orders = new ArrayList<>();
    private Long idCounter = 1L;

    private final DownstreamClient downstreamClient;

    public OrderService(DownstreamClient downstreamClient) {
        this.downstreamClient = downstreamClient;
    }

    public Order create(Order order) {
        Object[] users = downstreamClient.getUsers().join();

        if (users.length == 0) {
            throw new RuntimeException("User service is unavailable or has no users");
        }

        Food[] foodList = downstreamClient.getFoods().join();

        if (foodList.length == 0 && order.getItems() != null && !order.getItems().isEmpty()) {
            throw new RuntimeException("Food service is unavailable or has no foods");
        }

        order.setId(idCounter++);
        order.setStatus("CREATED");

        if (order.getItems() == null) {
            order.setItems(new HashMap<>());
        }

        orders.add(order);

        return order;
    }

    public List<Order> getAll() {
        return orders;
    }

    public Order addItem(Long orderId, Long foodId) {
        for (Order o : orders) {
            if (o.getId().equals(orderId)) {
                o.getItems().put(
                        foodId,
                        o.getItems().getOrDefault(foodId, 0) + 1
                );
                return o;
            }
        }
        return null;
    }

    public Order decreaseItem(Long orderId, Long foodId) {
        for (Order o : orders) {
            if (o.getId().equals(orderId)) {
                Integer quantity = o.getItems().get(foodId);

                if (quantity == null) {
                    return o;
                }

                if (quantity <= 1) {
                    o.getItems().remove(foodId);
                } else {
                    o.getItems().put(foodId, quantity - 1);
                }

                return o;
            }
        }
        return null;
    }

    public Order removeItem(Long orderId, Long foodId) {
        for (Order o : orders) {
            if (o.getId().equals(orderId)) {
                o.getItems().remove(foodId);
                return o;
            }
        }
        return null;
    }

    public Order markPaid(Long id) {
        for (Order o : orders) {
            if (o.getId().equals(id)) {
                o.setStatus("PAID");
                return o;
            }
        }
        return null;
    }
}
