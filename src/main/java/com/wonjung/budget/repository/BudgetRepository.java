package com.wonjung.budget.repository;

import com.wonjung.budget.entity.Budget;
import com.wonjung.budget.entity.Category;
import com.wonjung.budget.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long>, BudgetRepositoryCustom {

    @Query("select b from Budget b join fetch b.category")
    List<Budget> findAllByMemberWithCategory(Member member);

    @Query("select sum(b.amount) from Budget b where b.member = :member")
    Integer getAmountSumByMember(@Param("member") Member member);

    Optional<Budget> findByMemberAndCategory(Member member, Category category);
}
