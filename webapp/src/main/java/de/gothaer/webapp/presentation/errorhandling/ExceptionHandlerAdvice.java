package de.gothaer.webapp.presentation.errorhandling;


import de.gothaer.webapp.domain.PersonenServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {



    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getField() + "->" + x.getDefaultMessage())
                .collect(Collectors.toList());
        body.put("errors", errors);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(PersonenServiceException.class)
    public ResponseEntity<Object> handlePersonenServiceException (PersonenServiceException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("type", ex.getClass().getSimpleName());
        logger.error("Upps", ex);
        if(ex.getMessage().startsWith("Antipath"))
            return ResponseEntity.badRequest().body(body);
        return ResponseEntity.internalServerError().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericeException (Exception ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("type", ex.getClass().getSimpleName());
        logger.error("Unerwarter Fehler", ex);

        return ResponseEntity.internalServerError().body(body);
    }
}
