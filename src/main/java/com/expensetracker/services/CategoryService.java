package com.expensetracker.services;

import com.expensetracker.domain.Category;
import com.expensetracker.exceptions.BadRequestException;
import com.expensetracker.exceptions.ResourceNotFoundException;

import java.util.List;

public interface CategoryService {
    List<Category> fetchAllCategories(Integer userId, Integer categoryId);

    Category fetchCategoryById(Integer categoryId) throws ResourceNotFoundException;

    Category addCategory(Integer userId, String title, String description) throws BadRequestException;

    void updateCategory(Integer userId, Integer CategoryId, String title, String description) throws BadRequestException;

    void removeCategoryWithAllTransactions(Integer userId, Integer CategoryId) throws ResourceNotFoundException;
}
