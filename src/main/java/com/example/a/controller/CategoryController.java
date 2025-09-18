package com.example.a.controller;

import com.example.a.entity.Category;
import com.example.a.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{categoryId}")
    public Category getCategoryById(@PathVariable Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @PostMapping
    public String addCategory(@RequestBody Category category) {
        if (categoryService.addCategory(category)) {
            return "Category added successfully";
        }
        return "Failed to add category";
    }
    @GetMapping("/level/{categoryLevel}")
    public List<Category> getCategoriesByLevel(@PathVariable Integer categoryLevel) {
        return categoryService.getCategoriesByLevel(categoryLevel);
    }
    @PutMapping
    public String updateCategory(@RequestBody Category category) {
        if (categoryService.updateCategory(category)) {
            return "Category updated successfully";
        }
        return "Failed to update category";
    }

    @DeleteMapping("/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId) {
        if (categoryService.deleteCategory(categoryId)) {
            return "Category deleted successfully";
        }
        return "Failed to delete category";
    }

    @GetMapping("/search")
    public List<Category> searchCategoriesByName(@RequestParam String name) {
        return categoryService.searchCategoriesByName(name);
    }
}