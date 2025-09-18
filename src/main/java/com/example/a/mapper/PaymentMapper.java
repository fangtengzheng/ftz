package com.example.a.mapper;

import com.example.a.entity.Payment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentMapper {
    List<Payment> findAllByOrderId(String orderId);
    void insert(Payment payment);
    void updatePayment(Payment payment);
    Payment findByPaymentId(String paymentId);
}