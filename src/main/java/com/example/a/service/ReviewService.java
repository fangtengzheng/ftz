package com.example.a.service;

import com.example.a.entity.Review;
import java.util.List;

public interface ReviewService {
    void add(Review review);

    List<Review> listByProductId(Long productId);

    Double averageRating(Long productId);
}
