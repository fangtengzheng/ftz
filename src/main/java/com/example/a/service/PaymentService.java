package com.example.a.service;

import com.example.a.entity.Payment;
import java.util.List;

public interface PaymentService {
    List<Payment> findAllByOrderId(String orderId);
    void insert(Payment payment);
    void updatePayment(Payment payment);
    Payment findByPaymentId(String paymentId);
}