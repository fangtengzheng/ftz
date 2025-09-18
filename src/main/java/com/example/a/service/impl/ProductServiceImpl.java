package com.example.a.service.impl;

import com.example.a.entity.Product;
import com.example.a.mapper.ProductMapper;
import com.example.a.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> getAllProducts() {
        return productMapper.selectAllProducts();
    }

    @Override
    public Product getProductById(Long productId) {
        return productMapper.selectProductById(productId);
    }

    @Override
    public boolean addProduct(Product product) {
        return productMapper.insertProduct(product) > 0;
    }

    @Override
    public List<Product> getProductsByNameLike(String productName) {
        return productMapper.selectProductsByNameLike(productName);
    }
    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productMapper.selectProductsByCategoryId(categoryId);
    }
    @Override
    public boolean updateProduct(Product product) {
        return productMapper.updateProduct(product) > 0;
    }

    @Override
    public boolean deleteProduct(Long productId) {
        return productMapper.deleteProduct(productId) > 0;
    }
    
    @Override
    public List<Product> searchProducts(String keyword, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Integer isHot) {
        return productMapper.searchProducts(keyword, categoryId, minPrice, maxPrice, isHot);
    }
    
    @Override
    public List<Product> getHotProducts() {
        return productMapper.selectHotProducts();
    }
    
    @Override
    public boolean updateProductStatus(Long productId, Integer status) {
        return productMapper.updateProductStatus(productId, status) > 0;
    }
    
    @Override
    public boolean updateHotStatus(Long productId, Integer isHot) {
        return productMapper.updateHotStatus(productId, isHot) > 0;
    }
}