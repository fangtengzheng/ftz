package com.example.a.mapper;

import com.example.a.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderDetailMapper {
    List<OrderDetail> findAllByOrderId(String orderId);

    void insert(OrderDetail orderDetail);

    void updateReview(@Param("orderId") String orderId, @Param("productId") Long productId,
            @Param("rating") Integer rating, @Param("reviewDetail") String reviewDetail);

    List<Map<String, Object>> listReviewsByProductId(@Param("productId") Long productId);
}