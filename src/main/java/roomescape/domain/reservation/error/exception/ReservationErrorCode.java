package roomescape.domain.reservation.error.exception;

import org.springframework.http.HttpStatus;

public enum ReservationErrorCode {
    INVALID_RESERVATION_NAME_FORMAT_ERROR(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 형식의 이름이 입력되었습니다. 2-20자 이내의 영문, 한글, 띄어쓰기만 가능합니다."),
    INVALID_RESERVATION_DATE_FORMAT_ERROR(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 형식의 날짜가 입력되었습니다. 년, 월, 일을 정확히 입력해주세요."),
    INVALID_RESERVATION_DETAILS_ERROR(HttpStatus.BAD_REQUEST.value(), "예약의 정보가 올바르지 않습니다.");

    private final int status;
    private final String errorMessage;

    ReservationErrorCode(int status, String errorMessage) {
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
