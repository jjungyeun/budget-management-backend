package com.wonjung.budget.repository;

import com.wonjung.budget.entity.Budget;
import com.wonjung.budget.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query("select b from Budget b join fetch b.category")
    List<Budget> findAllByMemberWithCategory(Member member);
}
