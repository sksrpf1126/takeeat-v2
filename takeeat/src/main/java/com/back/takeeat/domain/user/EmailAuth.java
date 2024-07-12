package com.back.takeeat.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(value = AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailAuth {
    private static final Long EXPIRE_TIME = 10L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_auth_id")
    private Long emailAuthId;

    private String email;

    private String authCode;

    @CreatedDate
    private LocalDateTime createdTime;

    @Builder
    public EmailAuth(String email, String authCode) {
        this.email = email;
        this.authCode = authCode;
    }

    /**
     * 인증코드 만료 여부 체크
     * @return 만료(true), 아니라면(false)
     */
    public boolean expireCheck() {
        Long minutes = Duration.between(this.createdTime.toLocalTime(), LocalDateTime.now().toLocalTime()).toMinutes();

        return minutes >= EXPIRE_TIME ? true : false;
    }

}
