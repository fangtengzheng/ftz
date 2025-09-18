//package com.example.a.mapper;
//
//import com.example.a.entity.Cart;
//import org.apache.ibatis.annotations.Mapper;
//
//import java.util.List;
//
//@Mapper
//public interface CartMapper {
//    List<Cart> findAllByUserId(Long userId);
//    void clearCart(Long userId);
//    void insertToCart(Cart cart);
//    void updateQuantity(Long cartId, Integer quantity);
//    void deleteCart(Long cartId);
//}
package com.example.a.mapper;

import com.example.a.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CartMapper {
    // 修改1：增加userId参数
    List<Cart> findAllByUserId(Long userId);
    void clearCart(Long userId);
    void insertToCart(Cart cart);

    // 修改2：增加更新时间
    void updateQuantity(@Param("cartId") Long cartId,
                        @Param("quantity") Integer quantity,
                        @Param("updateTime") LocalDateTime updateTime);

    void deleteCart(Long cartId);

    // 新增3：商品存在检查
    Cart findByUserAndProduct(@Param("userId") Long userId,
                              @Param("productId") Long productId);

    // 新增4：归属验证查询
    Cart findById(Long cartId);
}