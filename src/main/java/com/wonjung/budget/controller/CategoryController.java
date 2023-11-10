package com.wonjung.budget.controller;

import com.wonjung.budget.dto.response.CategoriesResDto;
import com.wonjung.budget.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<CategoriesResDto> getCategories() {
        CategoriesResDto categories = categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }
}
