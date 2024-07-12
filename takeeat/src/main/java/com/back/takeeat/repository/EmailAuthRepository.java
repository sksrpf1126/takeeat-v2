package com.back.takeeat.repository;

import com.back.takeeat.domain.user.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {

    Optional<EmailAuth> findTop1ByEmailOrderByCreatedTimeDesc(String email);

}
