package roomescape.global.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.global.error.exception.UserException;

import java.util.List;

@RestControllerAdvice
public class ReservationExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> waitingExceptionHandler(UserException exception) {
        HttpStatus httpStatus = HttpStatus.valueOf(exception.getStatus());
        List<String> messages = List.of(exception.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(httpStatus, messages);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> runtimeExceptionHandler(RuntimeException exception) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        List<String> messages = List.of(exception.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(httpStatus, messages);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception exception) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        List<String> messages = List.of(exception.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(httpStatus, messages);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
