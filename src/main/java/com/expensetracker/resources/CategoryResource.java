package com.expensetracker.resources;

import com.expensetracker.domain.Category;
import com.expensetracker.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryResource {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(HttpServletRequest httpServletRequest) {
        int userId = (Integer) httpServletRequest.getAttribute("userId");
        List<Category> categoryList = categoryService.fetchAllCategories(userId);
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(HttpServletRequest httpServletRequest,
                                                @RequestBody Map<String, Object> categoryRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        String title = (String) categoryRequest.get("title");
        String description = (String) categoryRequest.get("description");
        Category category = categoryService.addCategory(userId, title, description);

        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(HttpServletRequest httpServletRequest, @PathVariable("categoryId") Integer categoryId) {
        int userId = (Integer) httpServletRequest.getAttribute("userId");
        Category category = categoryService.fetchCategoryById(userId, categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Map<String, String>> updateCategory(HttpServletRequest httpServletRequest,
                                                              @PathVariable("categoryId") Integer categoryId,
                                                              @RequestBody Category category) {
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        categoryService.updateCategory(userId, categoryId, category);

        Map<String, String> updatedResponse = new HashMap<>();
        updatedResponse.put("message", "Category updated success!");
        return new ResponseEntity<>(updatedResponse, HttpStatus.OK);
    }
}
