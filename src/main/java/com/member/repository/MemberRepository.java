package com.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.member.entity.Member;

/**
 * 팀원 엔티티의 데이터베이스 접근을 담당하는 Repository
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
}