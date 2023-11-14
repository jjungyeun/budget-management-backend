package com.wonjung.budget.config;

import com.wonjung.budget.dto.request.ExpenseCreateDto;
import com.wonjung.budget.dto.request.MemberCreateDto;
import com.wonjung.budget.entity.Category;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.repository.CategoryRepository;
import com.wonjung.budget.repository.MemberRepository;
import com.wonjung.budget.service.ExpenseService;
import com.wonjung.budget.service.MemberService;
import com.wonjung.budget.type.CategoryType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Profile("test-data")
@Component
@RequiredArgsConstructor
public class DbCategoryInit {

    private final MemberService memberService;
    private final ExpenseService expenseService;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @PostConstruct
    public void init() {
        saveCategories();
        saveExpenses();
    }

    private void saveCategories() {
        if (categoryRepository.findAll().isEmpty()) {
            List<Category> categories = new ArrayList<>();
            for (CategoryType type : CategoryType.values()) {
                categories.add(Category.builder()
                        .name(type.getKo())
                        .build());
            }
            categoryRepository.saveAll(categories);
        }
    }

    private void saveExpenses() {
        if (memberRepository.findByAccount("member_for_test_1").isPresent()) {
            return;
        }

        Member member = createMember("member_for_test_1");

        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(LocalDateTime.of(2023, 11, 9, 17, 12, 0))
                        .category("식비")
                        .amount(12000)
                        .memo("점심으로 마라탕")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(LocalDateTime.of(2023, 11, 1, 17, 12, 0))
                        .category("식비")
                        .amount(13000)
                        .memo("저녁으로 마라탕")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(LocalDateTime.of(2023, 10, 31, 10, 32, 0))
                        .category("교통")
                        .amount(3000)
                        .memo("강남 약속")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(LocalDateTime.of(2023, 11, 3, 12, 30, 0))
                        .category("생활")
                        .amount(2000)
                        .memo("다이소")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(LocalDateTime.of(2023, 11, 5, 9, 30, 0))
                        .category("기타")
                        .amount(2000000)
                        .memo("엄마한테 이체")
                        .isExcludedSum(true)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(LocalDateTime.of(2023, 11, 7, 19, 30, 0))
                        .category("교통")
                        .amount(12300)
                        .memo("시외버스 예매 (청주 마라탕 투어)")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(LocalDateTime.of(2023, 11, 1, 15, 30, 0))
                        .category("식비")
                        .amount(4500)
                        .memo("편의점에서 까까")
                        .isExcludedSum(false)
                        .build());

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime lastMonthDay = today.minusMonths(1);
        LocalDateTime lastWeekDay = today.minusWeeks(1);
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(lastMonthDay)
                        .category("식비")
                        .amount(12000)
                        .memo("점심으로 마라탕")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(lastMonthDay)
                        .category("식비")
                        .amount(17000)
                        .memo("저녁으로 마라탕")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(lastMonthDay)
                        .category("교통")
                        .amount(3300)
                        .memo("강남 약속")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(today)
                        .category("생활")
                        .amount(5000)
                        .memo("다이소")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(today)
                        .category("기타")
                        .amount(2000000)
                        .memo("엄마한테 이체")
                        .isExcludedSum(true)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(today)
                        .category("교통")
                        .amount(12000)
                        .memo("시외버스 예매 (청주 마라탕 투어)")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(today)
                        .category("식비")
                        .amount(4500)
                        .memo("편의점에서 까까")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(lastWeekDay)
                        .category("기타")
                        .amount(500)
                        .memo("이체 수수료")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(lastWeekDay)
                        .category("교통")
                        .amount(3500)
                        .memo("시외버스 예매 (청주 마라탕 투어)")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(lastWeekDay)
                        .category("식비")
                        .amount(2300)
                        .memo("편의점에서 까까")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(lastWeekDay)
                        .category("경조사")
                        .amount(42300)
                        .memo("동생 생일 선물")
                        .isExcludedSum(false)
                        .build());
    }

    private Member createMember(String memberAccount) {
        String memberPassword = "test123456!";
        String memberNickname = "테스트용 사용자";
        MemberCreateDto createDto = new MemberCreateDto(memberAccount, memberPassword, memberNickname, true);
        Long memberId = memberService.signup(createDto);
        return memberRepository.findById(memberId).get();
    }
}
