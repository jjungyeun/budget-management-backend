package com.wonjung.budget.repository;

import com.wonjung.budget.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
