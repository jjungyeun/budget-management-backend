package com.wonjung.budget.service.impl;

import com.wonjung.budget.dto.response.CategoriesResDto;
import com.wonjung.budget.repository.CategoryRepository;
import com.wonjung.budget.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoriesResDto getCategories() {
        List<CategoriesResDto.CategoryResDto> categories = categoryRepository.findAll()
                .stream()
                .map(it -> new CategoriesResDto.CategoryResDto(it.getId(), it.getName()))
                .toList();
        return new CategoriesResDto(categories);
    }
}
