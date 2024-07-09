package com.back.takeeat.common.exception;

public enum ErrorCode {

    MEMBER_NOT_FOUND(400, "U_001", "회원을 찾을 수 없습니다."),
    MEMBER_PASSWORD_MISMATCH(400, "U_002", "비밀번호가 일치하지 않습니다."),
    MEMBER_EXISTS(400, "U_003", "해당 아이디는 사용할 수 없습니다"),
    EMAIL_PROVIDER_MISMATCH_ERROR(400, "U_004", "이메일 또는 제공자가 일치하지 않습니다."),

    MARKET_NOT_FOUND(400, "M_001", "가게를 찾을 수 없습니다."),

    ORDER_NOT_FOUND(400, "O_001", "주문을 찾을 수 없습니다."),
    ORDER_STATUS_MISMATCH(400, "O_003", "주문 상태가 일치하지 않습니다."),
    ORDER_UNAUTHORIZED(403, "O_002", "해당 주문에 대한 권한이 없습니다."),

    MENU_NOT_FOUND(400, "MN_001", "메뉴를 찾을 수 없습니다."),

    NOTIFICATION_NOT_FOUND(400, "NO_001", "해당 알림을 찾을 수 없습니다"),
    NOTIFICATION_UNAUTHORIZED(403, "NO_002", "해당 알림에 대한 권한이 없습니다");

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
