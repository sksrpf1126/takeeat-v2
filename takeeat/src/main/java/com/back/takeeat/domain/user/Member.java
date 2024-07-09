package com.back.takeeat.domain.user;

import com.back.takeeat.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    private String name;

    private String email;

    private String pwd;

    private String phone;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Enumerated(EnumType.STRING)
    private MemberRoleType role;

    private String profile;

    private Boolean deleteYn;

    @Builder
    public Member(String nickname, String name, String email, String phone, ProviderType providerType, MemberRoleType role, String profile) {
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.providerType = providerType;
        this.role = role;
        this.profile = profile;
        this.deleteYn = false;
    }

    public void setEncryptPassword(String encryptPassword) {
        this.pwd = encryptPassword;
    }
}
