package com.back.takeeat.common.exception;

public enum ErrorCode {

    MEMBER_NOT_FOUND(400, "U_001", "회원을 찾을 수 없습니다."),
    MEMBER_PASSWORD_MISMATCH(400, "U_002", "비밀번호가 일치하지 않습니다."),
    MEMBER_EXISTS(400, "U_003", "해당 아이디는 사용할 수 없습니다"),
    EMAIL_PROVIDER_MISMATCH_ERROR(400, "U_004", "이메일 또는 제공자가 일치하지 않습니다."),
    MEMBER_UNAUTHORIZED(403, "U_005", "로그인이 필요한 서비스입니다."),
    MEMBER_ROLE_NOT_EXISTS(403, "U_006", "해당 계정은 권한이 존재하지 않습니다"),
    NO_SUCH_AUTH_CODE(500, "U_007", "인증 코드 발급에 문제가 발생했습니다."),
    FAIL_CONVERT(500, "U_008", "비정상적인 접근입니다."),

    MARKET_NOT_FOUND(400, "M_001", "가게를 찾을 수 없습니다."),
    MARKET_NOT_SAVE(400, "M_002", "마켓을 저장할 수 없습니다."),
    MARKET_IS_CLOSED(403, "M_003", "가게가 준비중입니다."),

    ORDER_NOT_FOUND(400, "O_001", "주문을 찾을 수 없습니다."),
    ORDER_STATUS_MISMATCH(400, "O_003", "주문 상태가 일치하지 않습니다."),
    ORDER_MENUS_MISMATCH(400, "O_004", "가게에 존재하지 않는 메뉴가 포함되어 있습니다."),
    ORDER_UNAUTHORIZED(404, "O_002", "해당 주문에 대한 권한이 없습니다."),

    PAYMENT_MONEY_NOT_EQUAL(400, "P_001", "결제금액이 일치하지 않습니다."),

    MENU_CATEGORY_NOT_FOUND(400, "MC_001", "메뉴 카테고리를 찾을 수 없습니다."),

    MENU_NOT_FOUND(400, "MN_001", "메뉴를 찾을 수 없습니다."),

    NOTIFICATION_NOT_FOUND(400, "NO_001", "해당 알림을 찾을 수 없습니다"),
    NOTIFICATION_UNAUTHORIZED(403, "NO_002", "해당 알림에 대한 권한이 없습니다"),

    CART_NOT_FOUND(400, "C_001", "장바구니를 찾을 수 없습니다"),

    CARTMENU_NOT_FOUND(400, "CM_001", "장바구니 메뉴를 찾을 수 없습니다"),

    REVIEW_NOT_FOUND(400, "R_001", "리뷰를 찾을 수 없습니다"),
    REVIEW_UNAUTHORIZED(403, "R_002", "해당 리뷰에 대한 권한이 없습니다"),

    OWNERREVIEW_NOT_FOUND(400, "OR_001", "리뷰의 답글을 찾을 수 없습니다");

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
