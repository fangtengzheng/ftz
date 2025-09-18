package com.example.a.controller;

import com.example.a.entity.Product;
import com.example.a.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @PostMapping
    public Map<String, Object> addProduct(@RequestBody Product product) {
        Map<String, Object> result = new HashMap<>();
        if (productService.addProduct(product)) {
            result.put("status", 200);
            result.put("msg", "Product added successfully");
        } else {
            result.put("status", 500);
            result.put("msg", "Failed to add product");
        }
        return result;
    }

    @PutMapping
    public Map<String, Object> updateProduct(@RequestBody Product product) {
        Map<String, Object> result = new HashMap<>();
        if (productService.updateProduct(product)) {
            result.put("status", 200);
            result.put("msg", "Product updated successfully");
        } else {
            result.put("status", 500);
            result.put("msg", "Failed to update product");
        }
        return result;
    }

    @GetMapping("/search")
    public List<Product> searchProductsByName(@RequestParam String productName) {
        return productService.getProductsByNameLike(productName);
    }

    @GetMapping("/search/advanced")
    public List<Product> advancedSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer isHot) {
        return productService.searchProducts(keyword, categoryId, minPrice, maxPrice, isHot);
    }

    @GetMapping("/hot")
    public List<Product> getHotProducts() {
        return productService.getHotProducts();
    }

    @DeleteMapping("/{productId}")
    public Map<String, Object> deleteProduct(@PathVariable Long productId) {
        Map<String, Object> result = new HashMap<>();
        if (productService.deleteProduct(productId)) {
            result.put("status", 200);
            result.put("msg", "Product deleted successfully");
        } else {
            result.put("status", 500);
            result.put("msg", "Failed to delete product");
        }
        return result;
    }

    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategoryId(@PathVariable Long categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }
    
    // 切换商品状态（上架/下架）
    @PutMapping("/{productId}/status")
    public Map<String, Object> toggleProductStatus(@PathVariable Long productId, @RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (productService.updateProductStatus(productId, status)) {
                result.put("status", 200);
                result.put("msg", "商品状态更新成功");
            } else {
                result.put("status", 500);
                result.put("msg", "商品状态更新失败");
            }
        } catch (Exception e) {
            result.put("status", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }
    
    // 切换热销状态
    @PutMapping("/{productId}/hot")
    public Map<String, Object> toggleHotStatus(@PathVariable Long productId, @RequestParam Integer isHot) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (productService.updateHotStatus(productId, isHot)) {
                result.put("status", 200);
                result.put("msg", "热销状态更新成功");
            } else {
                result.put("status", 500);
                result.put("msg", "热销状态更新失败");
            }
        } catch (Exception e) {
            result.put("status", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }
}