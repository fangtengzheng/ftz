package com.example.a.mapper;

import com.example.a.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {
    int insert(Review review);

    List<Review> listByProductId(@Param("productId") Long productId);

    Double averageRating(@Param("productId") Long productId);
}
