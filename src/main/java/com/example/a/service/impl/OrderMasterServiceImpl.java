package com.example.a.service.impl;

import com.example.a.entity.OrderMaster;
import com.example.a.mapper.OrderMasterMapper;
import com.example.a.service.OrderMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class OrderMasterServiceImpl implements OrderMasterService {

    @Autowired
    private OrderMasterMapper orderMasterMapper;

    @Override
    public List<OrderMaster> findAll() {
        return orderMasterMapper.findAll();
    }

    @Override
    public OrderMaster findById(String orderId) {
        return orderMasterMapper.findById(orderId);
    }

    @Override
    public void insert(OrderMaster orderMaster) {
        // 生成订单ID：时间戳 + 随机数
        if (orderMaster.getOrderId() == null || orderMaster.getOrderId().isEmpty()) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String random = String.format("%04d", new Random().nextInt(10000));
            orderMaster.setOrderId("ORDER" + timestamp + random);
        }
        
        // 设置创建时间
        if (orderMaster.getCreateTime() == null) {
            orderMaster.setCreateTime(LocalDateTime.now());
        }
        
        orderMasterMapper.insert(orderMaster);
    }

    @Override
    public void update(OrderMaster orderMaster) {
        orderMasterMapper.update(orderMaster);
    }

    @Override
    public List<Map<String, Object>> getUserOrdersWithDetails(Long userId) {
        return orderMasterMapper.getUserOrdersWithDetails(userId);
    }

    @Override
    public void confirmReceive(String orderId) {
        OrderMaster order = orderMasterMapper.findById(orderId);
        if (order != null) {
            order.setOrderStatus(2); // 已完成
            order.setReceiveTime(LocalDateTime.now());
            orderMasterMapper.update(order);
        }
    }

    @Override
    public void cancelOrder(String orderId) {
        OrderMaster order = orderMasterMapper.findById(orderId);
        if (order != null) {
            order.setOrderStatus(3); // 已取消
            order.setCancelTime(LocalDateTime.now());
            orderMasterMapper.update(order);
        }
    }

    @Override
    public void updatePayStatus(String orderId, int payStatus, int orderStatus, java.time.LocalDateTime paymentTime, java.time.LocalDateTime sendTime) {
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("orderId", orderId);
        params.put("payStatus", payStatus);
        params.put("orderStatus", orderStatus);
        params.put("paymentTime", paymentTime);
        params.put("sendTime", sendTime);
        orderMasterMapper.updatePayStatus(params);
    }

    @Override
    public void delete(String orderId) {
        orderMasterMapper.deletePaymentsByOrderId(orderId); // 先删支付
        orderMasterMapper.deleteOrderDetails(orderId);      // 再删明细
        orderMasterMapper.delete(orderId);                  // 最后删主表
    }
}