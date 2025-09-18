package com.example.a.service;

import com.example.a.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Long categoryId);
    boolean addCategory(Category category);
    boolean updateCategory(Category category);
    boolean deleteCategory(Long categoryId);
    List<Category> getCategoriesByLevel(Integer categoryLevel);
    List<Category> searchCategoriesByName(String name);
}