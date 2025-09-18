package com.example.a.service.impl;

import com.example.a.entity.Payment;
import com.example.a.mapper.PaymentMapper;
import com.example.a.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public List<Payment> findAllByOrderId(String orderId) {
        return paymentMapper.findAllByOrderId(orderId);
    }

    @Override
    public void insert(Payment payment) {
        paymentMapper.insert(payment);
    }

    @Override
    public void updatePayment(Payment payment) {
        paymentMapper.updatePayment(payment);
    }

    @Override
    public Payment findByPaymentId(String paymentId) {
        return paymentMapper.findByPaymentId(paymentId);
    }
}