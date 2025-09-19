package com.example.a.service;

import com.example.a.entity.OrderDetail;
import java.util.List;
import java.util.Map;

public interface OrderDetailService {
    List<OrderDetail> findAllByOrderId(String orderId);

    void insert(OrderDetail orderDetail);
    // 在 OrderDetailService 接口中添加
    List<Map<String, Object>> listReviewsByUserId(Long userId);

    void updateReview(String orderId, Long productId, Integer rating, String reviewDetail);

    List<Map<String, Object>> listReviewsByProductId(Long productId);
}