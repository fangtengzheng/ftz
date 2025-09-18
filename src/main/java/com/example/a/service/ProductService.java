package com.example.a.service;

import com.example.a.entity.Product;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long productId);
    boolean addProduct(Product product);
    boolean updateProduct(Product product);
    List<Product> getProductsByCategoryId(Long categoryId);
    List<Product> getProductsByNameLike(String productName);
    boolean deleteProduct(Long productId);
    
    // 新增搜索方法
    List<Product> searchProducts(String keyword, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Integer isHot);
    
    // 获取热销商品
    List<Product> getHotProducts();
    
    // 更新商品状态（上架/下架）
    boolean updateProductStatus(Long productId, Integer status);
    
    // 更新热销状态
    boolean updateHotStatus(Long productId, Integer isHot);
}