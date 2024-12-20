package hello.exception.exceptionhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exceptionhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * &#064;ControllerAdvice
 * 대상으로 지정한 여러 컨트롤러에 @ExceptionHandler, @InitBinder 기능을 부여
 * 대상을 지정하지 않으면 모든 컨트롤러에 적용(글로벌 적용)
 */
@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExControllerAdvice {
    private static final String ERROR_MESSAGE = "[exceptionHandle] ex";

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        log.error(ERROR_MESSAGE, e);

        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler // 예외 생략 시 메서드 파라미터의 예외가 자동으로 지정
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
        log.error(ERROR_MESSAGE, e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());

        return new ResponseEntity<>(errorResult, BAD_REQUEST);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error(ERROR_MESSAGE, e);

        return new ErrorResult("EX", "내부 오류");
    }
}
