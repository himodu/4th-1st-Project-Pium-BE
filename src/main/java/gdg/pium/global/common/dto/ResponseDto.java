package gdg.pium.global.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gdg.pium.global.exception.CommonException;
import gdg.pium.global.exception.ErrorCode;
import gdg.pium.global.exception.JsonWebTokenException;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public record ResponseDto<T>(@JsonIgnore HttpStatus httpStatus,
                             @NotNull Boolean success,
                             @Nullable T data,
                             @Nullable ExceptionDto error) {
    public static <T> ResponseDto<T> ok(@Nullable final T data) {
        return new ResponseDto<>(HttpStatus.OK, true, data, null);
    }

    public static <T> ResponseDto<T> created(@Nullable final T data) {
        return new ResponseDto<>(HttpStatus.CREATED, true, data, null);
    }

    public static <T> ResponseDto<T> noContent() {
        return new ResponseDto<>(HttpStatus.NO_CONTENT, true, null, null);
    }

    public static ResponseDto<Object> fail(final ConstraintViolationException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, false, null, new ArgumentNotValidExceptionDto(e));
    }

    public static ResponseDto<Object> fail(final UnexpectedTypeException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, false, null, ExceptionDto.of(ErrorCode.INVALID_PARAMETER_FORMAT));
    }

    public static ResponseDto<Object> fail(final MethodArgumentNotValidException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, false, null, new ArgumentNotValidExceptionDto(e));
    }

    public static ResponseDto<Object> fail(final MissingServletRequestParameterException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, false, null, ExceptionDto.of(ErrorCode.MISSING_REQUEST_PARAMETER));
    }

    public static ResponseDto<Object> fail(final MethodArgumentTypeMismatchException e) {
        return new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR, false, null, ExceptionDto.of(ErrorCode.INVALID_PARAMETER_FORMAT));
    }

    public static ResponseDto<Object> fail(final CommonException e) {
        return new ResponseDto<>(e.getErrorCode().getHttpStatus(), false, null, ExceptionDto.of(e.getErrorCode()));
    }

    public static ResponseDto<Object> fail(final JsonWebTokenException e) {
        return new ResponseDto<>(e.getErrorCode().getHttpStatus(), false, null, ExceptionDto.of(e.getErrorCode()));
    }
}
