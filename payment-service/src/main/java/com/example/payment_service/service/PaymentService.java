package com.example.payment_service.service;

import com.example.payment_service.client.OrderClient;
import com.example.payment_service.model.Payment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    private final OrderClient orderClient;
    private final List<Payment> history = new ArrayList<>();

    public PaymentService(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    public void process(Payment payment) {
        boolean markedPaid = orderClient.markPaid(payment.getOrderId()).join();

        payment.setStatus(markedPaid ? "SUCCESS" : "FAILED");
        history.add(payment);

        if (!markedPaid) {
            throw new RuntimeException("Order service is unavailable. Payment was not completed.");
        }

        System.out.println("Payment for order #" + payment.getOrderId() + " completed successfully");
    }

    public List<Payment> getHistory() {
        return history;
    }
}
