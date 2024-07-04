package com.back.takeeat.repository;

import com.back.takeeat.domain.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {


}
