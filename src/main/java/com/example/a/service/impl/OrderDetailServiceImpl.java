package com.example.a.service.impl;

import com.example.a.entity.OrderDetail;
import com.example.a.mapper.OrderDetailMapper;
import com.example.a.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public List<OrderDetail> findAllByOrderId(String orderId) {
        return orderDetailMapper.findAllByOrderId(orderId);
    }

    @Override
    public void insert(OrderDetail orderDetail) {
        orderDetailMapper.insert(orderDetail);
    }

    @Override
    public void updateReview(String orderId, Long productId, Integer rating, String reviewDetail) {
        orderDetailMapper.updateReview(orderId, productId, rating, reviewDetail);
    }

    @Override
    public List<Map<String, Object>> listReviewsByProductId(Long productId) {
        return orderDetailMapper.listReviewsByProductId(productId);
    }
}