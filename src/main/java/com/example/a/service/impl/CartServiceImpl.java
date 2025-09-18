package com.example.a.service.impl;

import com.example.a.entity.Cart;
import com.example.a.entity.Product;
import com.example.a.mapper.CartMapper;
import com.example.a.mapper.ProductMapper;
import com.example.a.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Cart> findAllByUserId(Long userId) {
        return cartMapper.findAllByUserId(userId);
    }

    @Override
    public void clearCart(Long userId) {
        cartMapper.clearCart(userId);
    }

    @Override
    @Transactional
    public void addToCart(Long userId, Long productId, Integer quantity) {
        // 检查商品是否已存在
        Cart existing = cartMapper.findByUserAndProduct(userId, productId);
        LocalDateTime now = LocalDateTime.now();

        if (existing != null) {
            // 存在则更新数量
            int newQuantity = existing.getQuantity() + quantity;
            cartMapper.updateQuantity(
                    existing.getCartId(),
                    newQuantity,
                    now
            );
        } else {
            // 不存在则新增
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            cart.setCreateTime(now);
            cart.setUpdateTime(now);
            cartMapper.insertToCart(cart);
        }
    }

    @Override
    @Transactional
    public void updateQuantity(Long userId, Long cartId, Integer quantity) {
        // 新增：前置验证
        if (quantity == null || quantity < 1) {
            throw new IllegalArgumentException("商品数量必须大于0");
        }

        // 归属验证
        Cart cart = cartMapper.findById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new SecurityException("无效的购物车项或访问被拒绝");
        }

        cartMapper.updateQuantity(cartId, quantity, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void deleteCart(Long userId, Long cartId) {
        // 增加归属验证
        Cart cart = cartMapper.findById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new SecurityException("Invalid cart item or access denied");
        }

        cartMapper.deleteCart(cartId);
    }

    @Override
    public Cart findByUserAndProduct(Long userId, Long productId) {
        return cartMapper.findByUserAndProduct(userId, productId);
    }

    @Override
    public List<Product> getCartProducts(Long userId) {
        List<Cart> carts = cartMapper.findAllByUserId(userId);
        List<Product> products = new ArrayList<>();
        for (Cart cart : carts) {
            // 修改这里：使用selectProductById方法
            Product product = productMapper.selectProductById(cart.getProductId());
            if (product != null) {
                products.add(product);
            }
        }
        return products;
    }

    @Override
    public Product getProductById(Long productId) {
        return productMapper.selectProductById(productId);
    }
}