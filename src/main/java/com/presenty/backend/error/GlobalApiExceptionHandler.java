package com.presenty.backend.error;

import com.presenty.backend.error.dto.ErrorResponse;
import com.presenty.backend.error.exception.BusinessException;
import com.presenty.backend.error.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalApiExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum 으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        log.error("handleMethodArgumentTypeMismatchException", ex);
        ErrorCode errorCode = ErrorCode.INVALID_TYPE_VALUE;
        ErrorResponse response = ErrorResponse.of(ex);
        return ResponseEntity
                .status(HttpStatus.valueOf(errorCode.getStatus()))
                .body(response);
    }

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     *
     * @ModelAttribute 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("handleMethodArgumentNotValid", ex);
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        ErrorResponse response = ErrorResponse.of(errorCode, ex.getBindingResult());
        return ResponseEntity
                .status(HttpStatus.valueOf(errorCode.getStatus()))
                .body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("handleAccessDeniedException", ex);
        ErrorCode errorCode = ErrorCode.HANDLE_ACCESS_DENIED;
        HttpStatus status = HttpStatus.valueOf(errorCode.getStatus());
        String message = errorCode.getMessage();
        ErrorResponse response = new ErrorResponse(errorCode.name(), message);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        log.error("handleBusinessException", ex);
        ErrorCode errorCode = ex.getErrorCode();
        HttpStatus status = HttpStatus.valueOf(errorCode.getStatus());
        ErrorResponse response = ErrorResponse.of(errorCode);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("handleException", ex);
        ErrorCode errorCode = ErrorCode.HANDLE_INTERNAL_SERVER_ERROR;
        HttpStatus status = HttpStatus.valueOf(errorCode.getStatus());
        String message = errorCode.getMessage();
        ErrorResponse response = new ErrorResponse(errorCode.name(), message);
        return ResponseEntity.status(status).body(response);
    }

}
