package com.back.takeeat.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PasswordFindRequest {

    @NotBlank(message = "아이디를 입력해 주세요")
    private String memberLoginId;


}
