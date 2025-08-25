package com.slash.music.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
    ResourceNotFoundException ex, WebRequest request
  ) {
    log.error("Resource not found: {}", ex.getMessage());

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.NOT_FOUND.value())
      .error("Resource Not Found")
      .message(ex.getMessage())
      .path(request.getDescription(false).replace("uri=", ""))
      .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
    DuplicateResourceException ex, WebRequest request
  ) {
    log.error("Duplicate resource: {}", ex.getMessage());

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.CONFLICT.value())
      .error("Resource Already Exists")
      .message(ex.getMessage())
      .path(request.getDescription(false).replace("uri=", ""))
      .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler({ BadCredentialsException.class, AuthenticationException.class })
  public ResponseEntity<ErrorResponse> handleAuthenticationException(
    Exception ex, WebRequest request
  ) {
    log.error("Authentication failed: {}", ex.getMessage());

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.UNAUTHORIZED.value())
      .error("Authentication Failed")
      .message("Invalid credentials")
      .path(request.getDescription(false).replace("uri=", ""))
      .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(
    AccessDeniedException ex, WebRequest request
  ) {
    log.error("Access denied: {}", ex.getMessage());

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.FORBIDDEN.value())
      .error("Access Denied")
      .message("You don't have permission to access this resource")
      .path(request.getDescription(false).replace("uri=", ""))
      .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler({ ExpiredJwtException.class })
  public ResponseEntity<ErrorResponse> handleExpiredJwtException(
    ExpiredJwtException ex, WebRequest request
  ) {
    log.error("JWT token expired: {}", ex.getMessage());

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.UNAUTHORIZED.value())
      .error("Token Expired")
      .message("JWT token has expired")
      .path(request.getDescription(false).replace("uri=", ""))
      .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({ MalformedJwtException.class, SecurityException.class })
  public ResponseEntity<ErrorResponse> handleJwtException(
    Exception ex, WebRequest request
  ) {
    log.error("JWT error: {}", ex.getMessage());

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.UNAUTHORIZED.value())
      .error("Invalid Token")
      .message("JWT token is invalid")
      .path(request.getDescription(false).replace("uri=", ""))
      .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
    MethodArgumentNotValidException ex, WebRequest request
  ) {
    log.error("Validation failed: {}", ex.getMessage());

    Map<String, String> validationErrors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      validationErrors.put(fieldName, errorMessage);
    });

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.BAD_REQUEST.value())
      .error("Validation Failed")
      .message("Input validation failed")
      .path(request.getDescription(false).replace("uri=", ""))
      .validationErrors(validationErrors)
      .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
    IllegalArgumentException ex, WebRequest request
  ) {
    log.error("Invalid argument: {}", ex.getMessage());

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.BAD_REQUEST.value())
      .error("Invalid Input")
      .message(ex.getMessage())
      .path(request.getDescription(false).replace("uri=", ""))
      .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(
    Exception ex, WebRequest request
  ) {
    log.error("Unexpected error occurred: ", ex);

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
      .error("Internal Server Error")
      .message("An unexpected error occurred")
      .path(request.getDescription(false).replace("uri=", ""))
      .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}