package com.back.takeeat.repository;

import com.back.takeeat.domain.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByMemberLoginId(String memberLoginId);

    boolean existsByEmail(String email);
    boolean existsByMemberLoginId(String memberLoginId);

}
