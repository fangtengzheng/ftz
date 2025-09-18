package com.example.a.controller;

import com.example.a.entity.OrderMaster;
import com.example.a.entity.OrderDetail;
import com.example.a.entity.Product;
import com.example.a.service.OrderMasterService;
import com.example.a.service.OrderDetailService;
import com.example.a.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderMasterController {

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/all")
    public List<OrderMaster> getAll() {
        return orderMasterService.findAll();
    }

    @GetMapping("/{orderId}")
    public OrderMaster getById(@PathVariable String orderId) {
        return orderMasterService.findById(orderId);
    }

    // 获取用户的订单列表
    @GetMapping("/user/{userId}")
    public Map<String, Object> getUserOrders(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> orders = orderMasterService.getUserOrdersWithDetails(userId);
            result.put("msg", "获取成功");
            result.put("orders", orders);
        } catch (Exception e) {
            e.printStackTrace(); // 打印详细错误信息到控制台
            result.put("msg", "获取订单失败: " + e.getMessage());
            result.put("error", e.getClass().getSimpleName());
        }
        return result;
    }

    @PostMapping
    public Map<String, Object> insert(@RequestBody OrderMaster orderMaster) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 设置订单初始状态
            orderMaster.setOrderStatus(0); // 待发货
            orderMaster.setPayStatus(0); // 待支付

            orderMasterService.insert(orderMaster);
            result.put("msg", "订单创建成功");
            result.put("orderId", orderMaster.getOrderId());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", 500);
            result.put("msg", "订单创建失败: " + e.getMessage());
        }
        return result;
    }

    // 创建完整订单（包括订单详情）
    @PostMapping("/create")
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> orderRequest) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 解析请求数据
            Long userId = Long.valueOf(orderRequest.get("userId").toString());
            Long addressId = Long.valueOf(orderRequest.get("addressId").toString());
            BigDecimal totalAmount = new BigDecimal(orderRequest.get("totalAmount").toString());
            BigDecimal shippingFee = new BigDecimal(orderRequest.get("shippingFee").toString());
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) orderRequest.get("items");

            // 生成订单ID
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String random = String.format("%04d", new Random().nextInt(10000));
            String orderId = "ORDER" + timestamp + random;

            // 创建订单主表
            OrderMaster orderMaster = new OrderMaster();
            orderMaster.setOrderId(orderId);
            orderMaster.setUserId(userId);
            orderMaster.setShippingId(addressId);
            orderMaster.setOrderTotal(totalAmount);
            orderMaster.setOrderStatus(0); // 待发货
            orderMaster.setPayStatus(0); // 待支付
            orderMaster.setPostage(shippingFee.intValue());
            orderMaster.setCreateTime(LocalDateTime.now());

            orderMasterService.insert(orderMaster);

            // 创建订单详情
            for (Map<String, Object> item : items) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(orderId);
                orderDetail.setProductId(Long.valueOf(item.get("productId").toString()));
                orderDetail.setQuantity(Integer.valueOf(item.get("quantity").toString()));
                orderDetail.setSellingPrice(new BigDecimal(item.get("price").toString()));
                orderDetail.setSubTotal(new BigDecimal(item.get("price").toString())
                        .multiply(new BigDecimal(item.get("quantity").toString())));

                // 添加商品信息（如果前端提供了）
                if (item.get("productName") != null) {
                    orderDetail.setProductName(item.get("productName").toString());
                }
                if (item.get("productImg") != null) {
                    orderDetail.setProductImg(item.get("productImg").toString());
                }
                if (item.get("specification") != null) {
                    orderDetail.setSpecification(item.get("specification").toString());
                }

                orderDetailService.insert(orderDetail);
            }

            result.put("msg", "订单创建成功");
            result.put("orderId", orderId);

        } catch (Exception e) {
            e.printStackTrace();

            result.put("msg", "订单创建失败: " + e.getMessage());
        }
        return result;
    }

    @PutMapping
    public String update(@RequestBody OrderMaster orderMaster) {
        orderMasterService.update(orderMaster);
        return "Order updated successfully";
    }

    // 确认收货
    @PostMapping("/confirm-receive")
    public Map<String, Object> confirmReceive(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String orderId = (String) request.get("orderId");
            orderMasterService.confirmReceive(orderId);
            result.put("msg", "确认收货成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "确认收货失败: " + e.getMessage());
        }
        return result;
    }

    // 取消订单
    @PostMapping("/cancel")
    public Map<String, Object> cancelOrder(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String orderId = (String) request.get("orderId");
            orderMasterService.cancelOrder(orderId);
            result.put("msg", "订单取消成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "取消订单失败: " + e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/{orderId}")
    public Map<String, Object> deleteOrder(@PathVariable String orderId) {
        Map<String, Object> result = new HashMap<>();
        try {
            orderMasterService.delete(orderId);
            result.put("msg", "订单删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "订单删除失败: " + e.getMessage());
        }
        return result;
    }

    // 对订单中的某个商品进行评价（星级1-5 + 文本），与确认收货独立
    @PostMapping("/review")
    public Map<String, Object> reviewProduct(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String orderId = (String) request.get("orderId");
            Long productId = Long.valueOf(request.get("productId").toString());
            Integer rating = Integer.valueOf(request.get("rating").toString());
            String reviewDetail = (String) request.get("reviewDetail");
            Long userId = null;
            String username = null;
            String avatar = null;
            if (request.get("userId") != null)
                userId = Long.valueOf(request.get("userId").toString());
            if (request.get("username") != null)
                username = request.get("username").toString();
            if (request.get("avatar") != null)
                avatar = request.get("avatar").toString();

            if (rating == null || rating < 1 || rating > 5) {
                throw new IllegalArgumentException("评级需为1-5");
            }

            // 可选校验：该商品确实在该订单中
            List<OrderDetail> details = orderDetailService.findAllByOrderId(orderId);
            boolean exists = details.stream()
                    .anyMatch(d -> d.getProductId() != null && d.getProductId().equals(productId));
            if (!exists) {
                throw new IllegalArgumentException("订单中不存在该商品");
            }

            // 按订单明细保存评价

            // 将评价写入订单明细
            orderDetailService.updateReview(orderId, productId, rating, reviewDetail);

            result.put("msg", "评价成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "评价失败: " + e.getMessage());
        }
        return result;
    }
}