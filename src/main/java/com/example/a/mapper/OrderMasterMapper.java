package com.example.a.mapper;

import com.example.a.entity.OrderMaster;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMasterMapper {
    List<OrderMaster> findAll();
    OrderMaster findById(String orderId);
    void insert(OrderMaster orderMaster);
    void update(OrderMaster orderMaster);
    
    // 获取用户的订单列表（包含订单详情和收货地址）
    List<Map<String, Object>> getUserOrdersWithDetails(Long userId);

    // 新增：只更新支付状态和订单状态
    void updatePayStatus(Map<String, Object> params);

    // 删除订单
    void delete(String orderId);

    // 删除订单明细
    void deleteOrderDetails(String orderId);

    // 删除订单支付记录
    void deletePaymentsByOrderId(String orderId);
}