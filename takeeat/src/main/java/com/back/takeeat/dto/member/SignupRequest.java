package com.back.takeeat.dto.member;

import com.back.takeeat.domain.user.Member;
import com.back.takeeat.domain.user.MemberRoleType;
import com.back.takeeat.domain.user.ProviderType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@Builder
public class SignupRequest {

    @NotBlank(message = "아이디를 입력해 주세요.")
    @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영문 소문자와 숫자 4~12자리여야 합니다.")
    private String memberLoginId;

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "8~20자의 숫자, 영문, 특수문자를 포함해주세요")
    private String password;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "8~20자의 숫자, 영문, 특수문자를 포함해주세요")
    private String passwordCheck;

    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;
    
    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$" , message = "닉네임은 특수문자를 포함하지 않은 2~10자리여야 합니다.")
    private String nickname;

    @NotBlank(message = "핸드폰 번호를 입력해 주세요.")
    private String phone;

    private ProviderType providerType;
    private String authCode;
    
    private MultipartFile profile;

    public Member toMember() {
        return Member.builder()
                .memberLoginId(this.memberLoginId)
                .email(this.email)
                .providerType(ProviderType.DEFAULT)
                .name(this.name)
                .nickname(this.nickname)
                .phone(this.phone)
                .role(MemberRoleType.ROLE_MEMBER)
                .build();
    }

}
