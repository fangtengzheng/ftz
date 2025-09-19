package com.example.a.controller;

import com.example.a.entity.OrderDetail;
import com.example.a.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/order-detail")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/{orderId}")
    public List<OrderDetail> getAllByOrderId(@PathVariable String orderId) {
        return orderDetailService.findAllByOrderId(orderId);
    }

    @PostMapping
    public String insert(@RequestBody OrderDetail orderDetail) {
        orderDetailService.insert(orderDetail);
        return "Order detail inserted successfully";
    }

    // 获取某个商品的全部评价（订单详情表 join 用户表）
    @GetMapping("/product/{productId}/reviews")
    public Map<String, Object> getReviewsByProductId(@PathVariable Long productId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<java.util.Map<String, Object>> data = orderDetailService.listReviewsByProductId(productId);
            result.put("status", 200);
            result.put("data", data);
        } catch (Exception e) {
            result.put("status", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }
    // 获取某个用户的全部评价（只展示已评价的商品）
    @GetMapping("/user/{userId}/reviews")
    public Map<String, Object> getReviewsByUserId(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> data = orderDetailService.listReviewsByUserId(userId);
            result.put("status", 200);
            result.put("data", data);
        } catch (Exception e) {
            result.put("status", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }
}