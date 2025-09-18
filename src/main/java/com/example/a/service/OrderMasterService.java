package com.example.a.service;

import com.example.a.entity.OrderMaster;
import java.util.List;
import java.util.Map;

public interface OrderMasterService {
    List<OrderMaster> findAll();
    OrderMaster findById(String orderId);
    void insert(OrderMaster orderMaster);
    void update(OrderMaster orderMaster);
    
    // 获取用户的订单列表（包含订单详情和收货地址）
    List<Map<String, Object>> getUserOrdersWithDetails(Long userId);
    
    // 确认收货
    void confirmReceive(String orderId);
    
    // 取消订单
    void cancelOrder(String orderId);
    
    // 新增：只更新支付状态和订单状态
    void updatePayStatus(String orderId, int payStatus, int orderStatus, java.time.LocalDateTime paymentTime, java.time.LocalDateTime sendTime);
    
    // 删除订单
    void delete(String orderId);
}