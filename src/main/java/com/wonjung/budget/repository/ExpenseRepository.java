package com.wonjung.budget.repository;

import com.wonjung.budget.entity.Expense;
import com.wonjung.budget.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Optional<Expense> findByIdAndMember(Long id, Member member);
}
