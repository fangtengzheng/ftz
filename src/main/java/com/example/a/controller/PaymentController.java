package com.example.a.controller;

import com.example.a.entity.Payment;
import com.example.a.entity.OrderMaster;
import com.example.a.service.PaymentService;
import com.example.a.service.OrderMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderMasterService orderMasterService;

    @GetMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> getAllByOrderId(@PathVariable String orderId) {
        try {
            List<Payment> payments = paymentService.findAllByOrderId(orderId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", payments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "获取支付记录失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> insert(@RequestBody Payment payment) {
        try {
            String paymentNo = "PN" + System.currentTimeMillis() + (int) (Math.random() * 10000);
            payment.setPaymentNo(paymentNo);
            paymentService.insert(payment);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "支付记录创建成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "创建支付记录失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> processPayment(@RequestBody Map<String, Object> paymentRequest) {
        try {
            String orderId = (String) paymentRequest.get("orderId");
            Integer paymentType = Integer.valueOf(paymentRequest.get("paymentType").toString());
            // 兼容前端可能不传金额的情况：优先取 amount，其次 paymentAmount，都没有则取订单总额，最后默认0
            Double paymentAmount = null;
            Object amountObj = paymentRequest.get("amount");
            Object paymentAmountObj = paymentRequest.get("paymentAmount");
            if (amountObj != null) {
                try {
                    paymentAmount = Double.valueOf(amountObj.toString());
                } catch (Exception ignore) {
                }
            }
            if (paymentAmount == null && paymentAmountObj != null) {
                try {
                    paymentAmount = Double.valueOf(paymentAmountObj.toString());
                } catch (Exception ignore) {
                }
            }
            if (paymentAmount == null) {
                OrderMaster order = orderMasterService.findById(orderId);
                if (order != null && order.getOrderTotal() != null) {
                    paymentAmount = order.getOrderTotal().doubleValue();
                } else {
                    paymentAmount = 0.0;
                }
            }

            // 创建支付记录
            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setPaymentType(paymentType);
            payment.setPaymentAmount(paymentAmount);
            payment.setPaymentStatus(0); // 0-处理中
            String paymentNo = "PN" + System.currentTimeMillis() + (int) (Math.random() * 10000);
            payment.setPaymentNo(paymentNo);

            paymentService.insert(payment);

            // 模拟支付处理
            // 在实际项目中，这里会调用第三方支付API
            boolean paymentSuccess = simulatePaymentProcess(paymentType, paymentAmount);

            if (paymentSuccess) {
                payment.setPaymentStatus(1); // 成功
                paymentService.updatePayment(payment);

                // 更新订单状态：支付成功并已发货（待收货）
                orderMasterService.updatePayStatus(orderId, 1, 1, LocalDateTime.now(), LocalDateTime.now());

                Map<String, Object> response = new HashMap<>();
                response.put("status", 200);
                response.put("msg", "支付成功，订单已发货");
                response.put("paymentId", payment.getPaymentId());
                return ResponseEntity.ok(response);
            } else {
                payment.setPaymentStatus(2); // 失败
                paymentService.updatePayment(payment);

                Map<String, Object> response = new HashMap<>();
                response.put("msg", "支付失败");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            System.err.println("支付接口异常，收到参数: " + paymentRequest);
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("msg", "支付处理失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/status/{paymentId}")
    public ResponseEntity<Map<String, Object>> getPaymentStatus(@PathVariable String paymentId) {
        try {
            Payment payment = paymentService.findByPaymentId(paymentId);
            if (payment != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("data", payment);
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "支付记录不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "获取支付状态失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/refund/{paymentId}")
    public ResponseEntity<Map<String, Object>> refundPayment(@PathVariable String paymentId) {
        try {
            Payment payment = paymentService.findByPaymentId(paymentId);
            if (payment != null && payment.getPaymentStatus() == 1) {
                payment.setPaymentStatus(3); // 退款
                paymentService.updatePayment(payment);

                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "退款成功");
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "无法退款，支付状态不正确");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "退款失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 模拟支付处理过程
    private boolean simulatePaymentProcess(Integer paymentType, Double paymentAmount) {
        // 模拟支付成功率
        // 在实际项目中，这里会调用真实的支付API
        try {
            Thread.sleep(1000); // 模拟网络延迟
            return Math.random() > 0.1; // 90%的成功率
        } catch (InterruptedException e) {
            return false;
        }
    }
}