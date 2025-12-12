package com.heang.drms_api.exception;
import com.heang.drms_api.common.api.ApiResponse;
import com.heang.drms_api.common.api.ExitCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🔹 1. Resource isn't found (ex: entity not in DB)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request
    ) {
        ApiResponse<Object> body = ApiResponse.error(
                ExitCode.SYSTEM_ERROR,   // later you can create specific code like NOT_FOUND
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    // 🔹 2. Bat aquest (invàlid parameter format, etc.)
    @ExceptionHandler(org.apache.coyote.BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequestException(
            org.apache.coyote.BadRequestException ex,
            WebRequest request
    ) {
        ApiResponse<Object> body = ApiResponse.error(
                ExitCode.REGISTRATION_FAILED,  // or create GENERIC_BAD_REQUEST
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    // 🔹 3. Validation errors (@Valid DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<Object> body = ApiResponse.error(
                ExitCode.REGISTRATION_FAILED,   // or create VALIDATION_ERROR code
                errors                          // 👉 return “field -> message” map
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    // 🔹 4. Authentication / login errors
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(
            Exception ex,
            WebRequest request
    ) {
        ApiResponse<Object> body = ApiResponse.error(
                ExitCode.INVALID_CREDENTIALS,
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(body);
    }

    // 🔹 5. Access denied (no permission)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(
            AccessDeniedException ex,
            WebRequest request
    ) {
        ApiResponse<Object> body = ApiResponse.error(
                ExitCode.INSUFFICIENT_PERMISSIONS,
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(body);
    }

    // 🔹 6. Custom BusinessException (you already did this well)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(
            BusinessException ex,
            WebRequest request
    ) {
        ApiResponse<Object> body = ApiResponse.error(
                ex.getErrorCode(),
                ex.getBody() != null ? ex.getBody() : ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.valueOf(ex.getErrorCode().getHttpCode()))
                .body(body);
    }

    // 🔹 7. Fallback – unknown error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(
            Exception ex,
            WebRequest request
    ) {
        ApiResponse<Object> body = ApiResponse.error(
                ExitCode.UNKNOWN_ERROR,
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
}




