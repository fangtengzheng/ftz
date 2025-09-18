package com.example.a.controller;

import com.example.a.entity.Cart;
import com.example.a.entity.Product;
import com.example.a.service.CartService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllByUserId(@RequestParam @Min(1) Long userId) {
        List<Cart> carts = cartService.findAllByUserId(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", carts);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearCart(@RequestParam @Min(1) Long userId) {
        cartService.clearCart(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Cart cleared successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestParam @Min(1) Long userId,
            @RequestParam @Min(1) Long productId,
            @RequestParam @Min(1) Integer quantity) {
        cartService.addToCart(userId, productId, quantity);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Product added to cart successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateQuantity(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("收到更新购物车请求: " + request);
            
            // 验证请求参数
            if (request.get("userId") == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            if (request.get("cartId") == null) {
                throw new IllegalArgumentException("购物车ID不能为空");
            }
            if (request.get("quantity") == null) {
                throw new IllegalArgumentException("数量不能为空");
            }
            
            Long userId = Long.valueOf(request.get("userId").toString());
            Long cartId = Long.valueOf(request.get("cartId").toString());
            Integer quantity = Integer.valueOf(request.get("quantity").toString());

            System.out.println("解析后的参数 - userId: " + userId + ", cartId: " + cartId + ", quantity: " + quantity);

            // 数量验证
            if (quantity < 1) {
                throw new IllegalArgumentException("商品数量必须大于0");
            }

            cartService.updateQuantity(userId, cartId, quantity);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "数量更新成功");
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            System.err.println("数字格式转换错误: " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "参数格式错误: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (IllegalArgumentException e) {
            System.err.println("参数验证错误: " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            System.err.println("更新购物车数量时发生错误: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "服务器内部错误: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteCart(
            @RequestParam @Min(1) Long userId,
            @RequestParam @Min(1) Long cartId) {
        cartService.deleteCart(userId, cartId);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Cart deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getCartProducts(@RequestParam @Min(1) Long userId) {
        List<Cart> carts = cartService.findAllByUserId(userId);
        List<Map<String, Object>> cartProducts = new ArrayList<>();

        for (Cart cart : carts) {
            Map<String, Object> cartProduct = new HashMap<>();
            // 购物车项字段
            cartProduct.put("cartId", cart.getCartId());
            cartProduct.put("userId", cart.getUserId());
            cartProduct.put("productId", cart.getProductId());
            cartProduct.put("quantity", cart.getQuantity());

            // 商品信息
            Product product = cartService.getProductById(cart.getProductId());
            if (product != null) {
                cartProduct.put("productName", product.getProductName());
                cartProduct.put("sellingPrice", product.getSellingPrice());
                cartProduct.put("productImg", product.getProductImg());
                // 你可以根据需要添加更多商品字段
            }

            cartProducts.add(cartProduct);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", cartProducts);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/table-info")
    public ResponseEntity<Map<String, Object>> getTableInfo() {
        Map<String, Object> response = new HashMap<>();
        try {
            // 这里我们直接查询表结构信息
            // 由于没有直接的SQL执行，我们先返回一些基本信息
            response.put("status", "success");
            response.put("message", "Cart表信息");
            response.put("tableName", "cart");
            response.put("expectedColumns", new String[]{"cartId", "userId", "productId", "quantity", "createTime", "updateTime"});
            
            // 测试查询一些数据
            List<Cart> testCarts = cartService.findAllByUserId(1L);
            response.put("testDataCount", testCarts.size());
            if (!testCarts.isEmpty()) {
                Cart firstCart = testCarts.get(0);
                response.put("sampleCart", firstCart);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "获取表信息失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testCartTable() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Cart> allCarts = cartService.findAllByUserId(1L); // 测试用户ID为1
            response.put("status", "success");
            response.put("message", "Cart表测试成功");
            response.put("totalCarts", allCarts.size());
            response.put("carts", allCarts);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Cart表测试失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(response);
        }
    }
}