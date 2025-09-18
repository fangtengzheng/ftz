package com.example.a.service.impl;

import com.example.a.entity.Review;
import com.example.a.mapper.ReviewMapper;
import com.example.a.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public void add(Review review) {
        reviewMapper.insert(review);
    }

    @Override
    public List<Review> listByProductId(Long productId) {
        return reviewMapper.listByProductId(productId);
    }

    @Override
    public Double averageRating(Long productId) {
        return reviewMapper.averageRating(productId);
    }
}
