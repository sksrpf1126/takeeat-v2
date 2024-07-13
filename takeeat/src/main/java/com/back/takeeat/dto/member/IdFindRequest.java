package com.back.takeeat.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class IdFindRequest {

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email
    private String email;
    @NotBlank(message = "인증코드를 입력해 주세요.")
    @Min(value = 6, message = "인증코드를 다시 확인해 주세요.")
    private String authCode;

}
