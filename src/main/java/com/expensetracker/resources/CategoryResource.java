package com.expensetracker.resources;

import com.expensetracker.domain.Category;
import com.expensetracker.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryResource {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public String getAllCategories(HttpServletRequest httpServletRequest) {
        int userId = (Integer) httpServletRequest.getAttribute("userId");
        return "User Id" + userId;
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
}
