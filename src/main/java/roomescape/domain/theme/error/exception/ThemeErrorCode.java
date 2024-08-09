package roomescape.domain.theme.error.exception;

import org.springframework.http.HttpStatus;

public enum ThemeErrorCode {
    INVALID_THEME_NAME_FORMAT_ERROR(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 형식의 이름이 입력되었습니다. 8-20자 이내의 영문, 한글, 숫자, 띄어쓰기만 가능합니다."),
    INVALID_THEME_DESCRIPTION_FORMAT_ERROR(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 형식의 설명이 입력되었습니다. 1-200자 이내의 영문, 한글, 숫자, 띄어쓰기만 가능합니다."),
    INVALID_THEME_THUMBNAIL_FORMAT_ERROR(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 형식의 썸네일 주소가 입력되었습니다. 'https://www.example.com/ex:7878'와 같은 형식만 가능합니다. 단 프로토콜과 포트번호, 도메인을 제외한 세부경로는 생략 가능합니다."),
    INVALID_THEME_DETAILS_ERROR(HttpStatus.BAD_REQUEST.value(), "테마의 정보가 올바르지 않습니다.");

    private final int status;
    private final String errorMessage;

    ThemeErrorCode(int status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
