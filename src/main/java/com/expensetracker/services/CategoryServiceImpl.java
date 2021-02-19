package com.expensetracker.services;

import com.expensetracker.domain.Category;
import com.expensetracker.exceptions.BadRequestException;
import com.expensetracker.exceptions.ResourceNotFoundException;
import com.expensetracker.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> fetchAllCategories(Integer userId) {
        return categoryRepository.findAllCategories(userId);
    }

    @Override
    public Category fetchCategoryById(Integer userId, Integer categoryId) throws ResourceNotFoundException {
        return categoryRepository.findById(userId, categoryId);
    }

    @Override
    public Category addCategory(Integer userId, String title, String description) throws BadRequestException {
        Integer categoryId = categoryRepository.create(userId, title, description);
        return categoryRepository.findById(userId, categoryId);
    }

    @Override
    public void updateCategory(Integer userId, Integer categoryId, Category category) throws BadRequestException {
        categoryRepository.updateCategory(userId, categoryId,category);
    }

    @Override
    public void removeCategoryWithAllTransactions(Integer userId, Integer categoryId) throws ResourceNotFoundException {
        fetchCategoryById(userId, categoryId);
        categoryRepository.removeCategoryById(userId, categoryId);
    }
}
