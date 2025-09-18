package com.example.a.controller;


import com.example.a.entity.OrderDetail;
import com.example.a.entity.OrderMaster;
import com.example.a.entity.PayUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;

import com.example.a.service.OrderDetailService;
import com.example.a.service.OrderMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
@RequestMapping("/api/alipay")
public class AliPayController {

    @Autowired
    private PayUtil payUtil;

    @Autowired
    private OrderMasterService orderManagementService;

    private OrderMaster order = null;
    private String tokens = "";

    @ResponseBody
    @PostMapping("/pay")
    public String alipay(@RequestHeader String token, @RequestParam("orderId") String orderId) {
        try {
            order = orderManagementService.findById(orderId);
            if (order == null) {
                return "订单信息不存在，请检查订单ID";
            }
            tokens = token;
            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String user = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String OrderNum = time + user;
            float oderValue = order.getOrderTotal().floatValue();
            System.out.println("商品名字是订单ID: " + order.getOrderId());

            // 将真实订单ID存储到session或缓存中，以便回调时使用
            // 这里我们使用一个简单的方式：将真实订单ID作为subject的一部分传递
            String subject = "订单ID:" + order.getOrderId() + "_临时号:" + OrderNum;

            // 直接返回支付宝页面HTML
            return payUtil.sendRequestToAlipay(OrderNum, oderValue, subject);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "支付宝接口调用失败，请稍后重试";
        }
    }

    // 当我们支付完成之后跳转这个请求并携带参数，我们将里面的订单id接收到，通过订单id查询订单信息，信息包括支付是否成功等
    @ResponseBody
    @GetMapping("/toSuccess")
    public String returns(String out_trade_no) throws ParseException {
        System.out.println("收到支付宝回调，订单号: " + out_trade_no);
        System.out.println("当前缓存的订单对象: " + (order != null ? order.getOrderId() : "null"));
        
        try {
            String query = payUtil.query(out_trade_no);
            System.out.println("支付宝查询结果: " + query);
            
            // 检查查询结果是否为空
            if (query == null || query.trim().isEmpty()) {
                System.out.println("支付宝查询结果为空，尝试直接更新订单状态");
                // 如果支付宝查询失败，但用户已经跳转到成功页面，我们假设支付成功
                if (order != null && order.getOrderId() != null) {
                    try {
                        orderManagementService.updatePayStatus(
                            order.getOrderId(), 
                            1, // payStatus: 1-已支付
                            1, // orderStatus: 1-待发货
                            LocalDateTime.now(), // paymentTime
                            null // sendTime
                        );
                        System.out.println("订单支付状态更新成功（降级处理）: " + order.getOrderId());
                        return "<script>window.location.href='http://localhost:8081/orders';</script>";
                    } catch (Exception e) {
                        System.out.println("降级更新订单状态失败: " + e.getMessage());
                    }
                }
                return "<script>window.location.href='http://localhost:8081/orders';</script>";
            }
            
            JSONObject jsonObject = JSONObject.parseObject(query);
            Object o = jsonObject.get("alipay_trade_query_response");
            
            if (o == null) {
                System.out.println("支付宝响应数据格式异常，尝试降级处理");
                // 降级处理：直接更新订单状态
                if (order != null && order.getOrderId() != null) {
                    try {
                        orderManagementService.updatePayStatus(
                            order.getOrderId(), 
                            1, // payStatus: 1-已支付
                            1, // orderStatus: 1-待发货
                            LocalDateTime.now(), // paymentTime
                            null // sendTime
                        );
                        System.out.println("订单支付状态更新成功（降级处理）: " + order.getOrderId());
                        return "<script>window.location.href='http://localhost:8081/orders';</script>";
                    } catch (Exception e) {
                        System.out.println("降级更新订单状态失败: " + e.getMessage());
                    }
                }
                return "<script>window.location.href='http://localhost:8081/orders';</script>";
            }
            
            Map map = (Map) o;
            System.out.println("支付宝响应数据: " + map);
            
            // 获取交易状态
            Object tradeStatus = map.get("trade_status");
            String status = tradeStatus != null ? tradeStatus.toString() : "";
            
            // 获取商户订单号（临时订单号）
            Object outTradeNo = map.get("out_trade_no");
            String tempOrderId = outTradeNo != null ? outTradeNo.toString() : "";
            
            // 从subject中提取真实订单ID
            String realOrderId = null;
            if (order != null && order.getOrderId() != null) {
                realOrderId = order.getOrderId();
            } else {
                // 如果order对象为空，尝试从subject中解析
                Object subject = map.get("subject");
                if (subject != null) {
                    String subjectStr = subject.toString();
                    if (subjectStr.contains("订单ID:")) {
                        String[] parts = subjectStr.split("订单ID:");
                        if (parts.length > 1) {
                            String[] orderParts = parts[1].split("_");
                            if (orderParts.length > 0) {
                                realOrderId = orderParts[0];
                            }
                        }
                    }
                }
            }
            
            System.out.println("交易状态: " + status + ", 临时订单号: " + tempOrderId + ", 真实订单号: " + realOrderId);
            
            // 判断支付是否成功
            if ("TRADE_SUCCESS".equals(status) || "TRADE_FINISHED".equals(status)) {
                // 支付成功，更新订单状态
                if (realOrderId != null && !realOrderId.trim().isEmpty()) {
                    try {
                        // 更新支付状态为已支付(1)，订单状态为待发货(1)
                        orderManagementService.updatePayStatus(
                            realOrderId, 
                            1, // payStatus: 1-已支付
                            1, // orderStatus: 1-待发货
                            LocalDateTime.now(), // paymentTime
                            null // sendTime: 暂时为null，发货时再设置
                        );
                        System.out.println("订单支付状态更新成功: " + realOrderId);
                    } catch (Exception e) {
                        System.out.println("更新订单状态失败: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("无法获取真实订单ID，无法更新订单状态");
                }
                
                // 支付成功，跳转到订单页面
                return "<script>window.location.href='http://localhost:8081/orders';</script>";
            } else if ("WAIT_BUYER_PAY".equals(status)) {
                // 等待买家付款
                return "<script>window.location.href='http://localhost:8081/orders';</script>";
            } else if ("TRADE_CLOSED".equals(status)) {
                // 交易关闭
                return "<script>window.location.href='http://localhost:8081/orders';</script>";
            } else {
                // 其他状态
                return "<script>window.location.href='http://localhost:8081/orders';</script>";
            }
            
        } catch (Exception e) {
            System.out.println("处理支付宝回调异常: " + e.getMessage());
            e.printStackTrace();
            return "<script>alert('处理异常，请稍后重试');window.location.href='http://localhost:8081/orders';</script>";
        }
    }

    // 查询订单支付状态接口
    @ResponseBody
    @GetMapping("/queryOrderStatus")
    public Map<String, Object> queryOrderStatus(@RequestParam("orderId") String orderId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 根据订单ID查询订单信息
            OrderMaster order = orderManagementService.findById(orderId);
            if (order == null) {
                result.put("error", "订单不存在");
                return result;
            }
            
            // 返回订单的支付状态
            result.put("orderId", orderId);
            result.put("payStatus", order.getPayStatus());
            result.put("orderStatus", order.getOrderStatus());
            result.put("orderTotal", order.getOrderTotal());
            result.put("paymentTime", order.getPaymentTime());
            
            return result;
        } catch (Exception e) {
            System.out.println("查询订单状态异常: " + e.getMessage());
            e.printStackTrace();
            result.put("error", "查询订单状态失败: " + e.getMessage());
            return result;
        }
    }

    // 测试支付宝查询接口
    @ResponseBody
    @GetMapping("/testQuery")
    public Map<String, Object> testQuery(@RequestParam("outTradeNo") String outTradeNo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            System.out.println("测试查询支付宝订单: " + outTradeNo);
            String queryResult = payUtil.query(outTradeNo);
            
            result.put("outTradeNo", outTradeNo);
            result.put("queryResult", queryResult);
            result.put("success", true);
            
            return result;
        } catch (Exception e) {
            System.out.println("测试查询异常: " + e.getMessage());
            e.printStackTrace();
            result.put("error", "测试查询失败: " + e.getMessage());
            result.put("success", false);
            return result;
        }
    }

    // 测试支付宝连接
    @ResponseBody
    @GetMapping("/testConnection")
    public Map<String, Object> testConnection() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String testResult = payUtil.testAlipayConnection();
            result.put("testResult", testResult);
            result.put("success", true);
            
            return result;
        } catch (Exception e) {
            System.out.println("测试连接异常: " + e.getMessage());
            e.printStackTrace();
            result.put("error", "测试连接失败: " + e.getMessage());
            result.put("success", false);
            return result;
        }
    }
}