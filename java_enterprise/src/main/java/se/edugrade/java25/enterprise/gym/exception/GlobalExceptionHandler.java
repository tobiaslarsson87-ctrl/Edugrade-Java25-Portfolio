package se.edugrade.java25.enterprise.gym.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<Map<String, Object>> exResponse(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("message", message);
        body.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(GymClassNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleGymClassNotFound(GymClassNotFoundException ex) {
        log.warn(ex.getMessage());
        return exResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBookingNotFound (BookingNotFoundException ex) {
        log.warn(ex.getMessage());
        return exResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CapacityExceededException.class)
    public ResponseEntity<Map<String, Object>> handleCapacityExceeded(CapacityExceededException ex) {
        log.warn(ex.getMessage());
        return exResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                        .map(e -> e.getField() + "|" + e.getDefaultMessage())
                        .collect(Collectors.joining(", "));

        log.warn(message);
        return exResponse(HttpStatus.BAD_REQUEST, message);
    }

    //fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleFallback(Exception ex) {
        log.warn(ex.getMessage());
        return exResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
