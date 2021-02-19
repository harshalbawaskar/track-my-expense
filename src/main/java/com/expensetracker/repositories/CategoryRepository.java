package com.expensetracker.repositories;

import com.expensetracker.domain.Category;
import com.expensetracker.exceptions.BadRequestException;
import com.expensetracker.exceptions.ResourceNotFoundException;

import java.util.List;

public interface CategoryRepository {
    Integer create(Integer userId, String title, String description) throws BadRequestException;

    void updateCategory(Integer userId, Integer categoryId, Category category) throws BadRequestException;

    List<Category> findAllCategories(Integer userId) throws ResourceNotFoundException;

    Category findById(Integer userId, Integer categoryId) throws ResourceNotFoundException;

    void removeCategoryById(Integer userId, Integer categoryId) throws ResourceNotFoundException;
}
