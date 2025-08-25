package com.slash.music.controller;

import com.slash.music.dto.*;
import com.slash.music.security.CustomUserDetails;
import com.slash.music.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "APIs for user authentication and authorization")
public class AuthController {
  private final AuthService authService;

  @Operation(summary = "Register a new user", description = "Creates a new user account and returns JWT tokens")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "User registered successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = AuthResponse.class)
        )),
    @ApiResponse(responseCode = "400", description = "Invalid input data"),
    @ApiResponse(responseCode = "409", description = "Email or username already exists")
  })
  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequestDTO request) {
    log.info("Registration request received for email: {}", request.getEmail());
    AuthResponse response = authService.register(request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @Operation(summary = "User login", description = "Authenticates user credentials and returns JWT tokens")
  @ApiResponses(value = {
      @ApiResponse(
        responseCode = "200",
        description = "Login successful",
        content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = AuthResponse.class)
          )),
      @ApiResponse(responseCode = "401", description = "Invalid credentials")
  })
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequestDTO request) {
    log.info("Login request received for email: {}", request.getEmail());
    AuthResponse response = authService.login(request);
    return ResponseEntity.ok(response);
  }

  @Operation(
    summary = "Refresh JWT token",
    description = "Generates new access and refresh tokens using a valid refresh token"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
        description = "Token refreshed successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = AuthResponse.class)
          )),
      @ApiResponse(responseCode = "401", description = "Invalid refresh token")
  })
  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
    log.info("Token refresh request received");
    AuthResponse response = authService.refreshToken(request);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Get current user", description = "Returns current authenticated user information")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
        description = "Current user retrieved successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = UserResponseDTO.class)
          )),
      @ApiResponse(responseCode = "401", description = "User not authenticated")
  })
  @SecurityRequirement(name = "bearerAuth")
  @GetMapping("/me")
  public ResponseEntity<UserResponseDTO> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

    log.info("Getting current user info for: {}", userDetails.getEmail());
    UserResponseDTO user = authService.getCurrentUser(userDetails.getEmail());
    return ResponseEntity.ok(user);
  }

  @Operation(summary = "Change password", description = "Changes the current user's password")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Password changed successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid password data"),
      @ApiResponse(responseCode = "401", description = "User not authenticated or current password incorrect")
  })
  @SecurityRequirement(name = "bearerAuth")
  @PostMapping("/change-password")
  public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequestDTO request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

    log.info("Password change request for user: {}", userDetails.getEmail());
    authService.changePassword(userDetails.getId(), request);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Logout", description = "Invalidates the current session (client should discard tokens)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Logout successful")
  })
  @SecurityRequirement(name = "bearerAuth")
  @PostMapping("/logout")
  public ResponseEntity<Void> logout() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
      log.info("Logout request for user: {}", userDetails.getEmail());
    }

    // In a stateless JWT implementation, logout is handled client-side
    // The client should discard the tokens
    // For enhanced security, you could implement a token blacklist
    return ResponseEntity.ok().build();
  }
}