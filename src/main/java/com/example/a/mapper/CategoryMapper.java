package com.example.a.mapper;

import com.example.a.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> selectAllCategories();
    Category selectCategoryById(@Param("categoryId") Long categoryId);
    int insertCategory(Category category);
    int updateCategory(Category category);
    int deleteCategory(@Param("categoryId") Long categoryId);
    List<Category> selectCategoriesByLevel(@Param("categoryLevel") Integer categoryLevel);
    List<Category> searchCategoriesByName(@Param("categoryName") String categoryName);
}