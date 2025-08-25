package com.slash.music.service;

import com.slash.music.dto.*;
import com.slash.music.exception.ResourceNotFoundException;
import com.slash.music.exception.DuplicateResourceException;
import com.slash.music.model.User;
import com.slash.music.model.UserRole;
import com.slash.music.model.UserStatus;
import com.slash.music.repository.UserRepository;
import com.slash.music.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final UserMapper userMapper;

  public AuthResponse register(RegisterRequestDTO request) {
    log.info("Registering new user with email: {}", request.getEmail());

    // Validate password confirmation
    if (!request.getPassword().equals(request.getConfirmPassword())) {
      throw new IllegalArgumentException("Password and confirm password do not match");
    }

    // Check for duplicate email
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new DuplicateResourceException("Email already exists: " + request.getEmail());
    }

    // Check for duplicate username
    if (userRepository.existsByName(request.getName())) {
      throw new DuplicateResourceException("Username already exists: " + request.getName());
    }

    // Create new user
    User user = new User();
    user.setEmail(request.getEmail());
    user.setName(request.getName());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(UserRole.FREE_USER);
    user.setStatus(UserStatus.ACTIVE);

    User savedUser = userRepository.save(user);
    log.info("Successfully registered user with ID: {}", savedUser.getId());

    // Generate tokens
    UserDetails userDetails = CustomUserDetails.create(savedUser);
    String accessToken = jwtService.generateToken(userDetails);
    String refreshToken = jwtService.generateRefreshToken(userDetails);

    return AuthResponse.builder()
      .accessToken(accessToken)
      .refreshToken(refreshToken)
      .user(userMapper.toResponseDTO(savedUser))
      .build();
  }

  public AuthResponse login(LoginRequestDTO request) {
    log.info("Authenticating user with email: {}", request.getEmail());

    try {
      // Authenticate user
      Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          request.getEmail(),
          request.getPassword()));

      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

      // Find user entity for response
      User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

      // Generate tokens
      String accessToken = jwtService.generateToken(userDetails);
      String refreshToken = jwtService.generateRefreshToken(userDetails);

      log.info("Successfully authenticated user: {}", request.getEmail());

      return AuthResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .user(userMapper.toResponseDTO(user))
        .build();

    } catch (Exception e) {
      log.error("Authentication failed for user {}: {}", request.getEmail(), e.getMessage());
      throw new BadCredentialsException("Invalid email or password");
    }
  }

  public AuthResponse refreshToken(RefreshTokenRequestDTO request) {
    log.info("Refreshing token");

    try {
      String userEmail = jwtService.extractUsername(request.getRefreshToken());
      UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

      if (jwtService.isTokenValid(request.getRefreshToken(), userDetails)) {
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        User user = userRepository.findByEmail(userEmail)
          .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        log.info("Successfully refreshed token for user: {}", userEmail);

        return AuthResponse.builder()
          .accessToken(accessToken)
          .refreshToken(refreshToken)
          .user(userMapper.toResponseDTO(user))
          .build();
      } else {
        throw new BadCredentialsException("Invalid refresh token");
      }
    } catch (Exception e) {
      log.error("Token refresh failed: {}", e.getMessage());
      throw new BadCredentialsException("Invalid refresh token");
    }
  }

  public void changePassword(Long userId, ChangePasswordRequestDTO request) {
    log.info("Changing password for user ID: {}", userId);

    // Validate password confirmation
    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
      throw new IllegalArgumentException("New password and confirm password do not match");
    }

    User user = userRepository.findById(userId)
      .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    // Verify current password
    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new BadCredentialsException("Current password is incorrect");
    }

    // Update password
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);

    log.info("Successfully changed password for user ID: {}", userId);
  }

  @Transactional(readOnly = true)
  public UserResponseDTO getCurrentUser(String email) {
    log.debug("Getting current user with email: {}", email);

    User user = userRepository.findByEmail(email)
      .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    return userMapper.toResponseDTO(user);
  }
}