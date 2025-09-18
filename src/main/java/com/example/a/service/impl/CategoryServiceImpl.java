package com.example.a.service.impl;

import com.example.a.entity.Category;
import com.example.a.mapper.CategoryMapper;
import com.example.a.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getAllCategories() {

        List<Category>  categoryList = categoryMapper.selectAllCategories();
        return  categoryList;
    }
    @Override
    public List<Category> getCategoriesByLevel(Integer categoryLevel) {
        return categoryMapper.selectCategoriesByLevel(categoryLevel);
    }
    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryMapper.selectCategoryById(categoryId);
    }

    @Override
    public boolean addCategory(Category category) {
        return categoryMapper.insertCategory(category) > 0;
    }

    @Override
    public boolean updateCategory(Category category) {
        return categoryMapper.updateCategory(category) > 0;
    }

    @Override
    public boolean deleteCategory(Long categoryId) {
        return categoryMapper.deleteCategory(categoryId) > 0;
    }

    @Override
    public List<Category> searchCategoriesByName(String name) {
        return categoryMapper.searchCategoriesByName(name);
    }
}