package com.back.takeeat.dto.member;

import com.back.takeeat.domain.user.Member;
import com.back.takeeat.domain.user.MemberRoleType;
import com.back.takeeat.domain.user.ProviderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@Builder
public class SignupRequest {

    private String email;
    private String password;
    private String passwordCheck;
    private ProviderType providerType;
    private String name;
    private String nickname;
    private String phone;
    private MultipartFile profile;

    public Member toMember() {
        return Member.builder()
                .email(this.email)
                .providerType(ProviderType.DEFAULT)
                .name(this.name)
                .nickname(this.nickname)
                .phone(this.phone)
                .role(MemberRoleType.ROLE_MEMBER)
                .build();
    }

    public Member toMemberFromSocial() {
        return Member.builder()
                .email(this.email)
                .providerType(this.providerType)
                .name(this.name)
                .nickname(this.nickname)
                .phone(this.phone)
                .role(MemberRoleType.ROLE_MEMBER)
                .build();

    }

}
