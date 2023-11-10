package com.wonjung.budget.config;

import com.wonjung.budget.entity.Category;
import com.wonjung.budget.repository.CategoryRepository;
import com.wonjung.budget.type.CategoryType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Profile("test-data")
@Component
@RequiredArgsConstructor
public class DbCategoryInit {

    private final CategoryRepository categoryRepository;

    @Transactional
    @PostConstruct
    public void init() {
        List<Category> categories = new ArrayList<>();
        for (CategoryType type : CategoryType.values()) {
            categories.add(Category.builder()
                    .name(type.getKo())
                    .build());
        }
        categoryRepository.saveAll(categories);
    }
}
