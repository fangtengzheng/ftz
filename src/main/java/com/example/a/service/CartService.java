package com.example.a.service;

import com.example.a.entity.Cart;
import com.example.a.entity.Product;
import java.util.List;

public interface CartService {
    // 修改1：增加userId参数
    List<Cart> findAllByUserId(Long userId);
    void clearCart(Long userId);
    void addToCart(Long userId, Long productId, Integer quantity);

    // 修改2：增加userId参数
    void updateQuantity(Long userId, Long cartId, Integer quantity);

    // 修改3：增加userId参数
    void deleteCart(Long userId, Long cartId);

    // 新增4：用于商品存在检查的方法
    Cart findByUserAndProduct(Long userId, Long productId);

    // 新增：获取用户购物车中的所有商品信息
    List<Product> getCartProducts(Long userId);

    Product getProductById(Long productId);
}