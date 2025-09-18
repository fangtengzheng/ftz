package com.example.a.mapper;

import com.example.a.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ProductMapper {
    List<Product> selectAllProducts();
    Product selectProductById(Long productId);
    List<Product> selectProductsByCategoryId(Long categoryId);
    int insertProduct(Product product);
    int updateProduct(Product product);
    int deleteProduct(Long productId);
    List<Product> selectProductsByNameLike(String productName);
    
    // 新增搜索方法
    List<Product> searchProducts(@Param("keyword") String keyword, 
                                @Param("categoryId") Long categoryId,
                                @Param("minPrice") BigDecimal minPrice,
                                @Param("maxPrice") BigDecimal maxPrice,
                                @Param("isHot") Integer isHot);
    
    // 获取热销商品
    List<Product> selectHotProducts();
    
    // 更新商品状态（上架/下架）
    int updateProductStatus(@Param("productId") Long productId, @Param("status") Integer status);
    
    // 更新热销状态
    int updateHotStatus(@Param("productId") Long productId, @Param("isHot") Integer isHot);
}