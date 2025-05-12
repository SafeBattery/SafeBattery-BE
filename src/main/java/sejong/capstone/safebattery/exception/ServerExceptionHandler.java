package sejong.capstone.safebattery.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ServerExceptionHandler {

    @ExceptionHandler(AiServerException.class)
    public ResponseEntity<String> handleAIServerException(AiServerException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body("AI 서버와 통신 중 에러 발생 : " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherExceptions(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 오류 발생");
    }
}
