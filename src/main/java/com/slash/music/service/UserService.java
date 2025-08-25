package com.slash.music.service;

import com.slash.music.dto.UserCreateDTO;
import com.slash.music.dto.UserMapper;
import com.slash.music.dto.UserResponseDTO;
import com.slash.music.dto.UserUpdateDTO;
import com.slash.music.exception.ResourceNotFoundException;
import com.slash.music.exception.DuplicateResourceException;
import com.slash.music.model.User;
import com.slash.music.model.UserRole;
import com.slash.music.model.UserStatus;
import com.slash.music.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder; // You'll need to configure this
  private final UserMapper userMapper;

  /**
   * Create a new user
   */
  public UserResponseDTO createUser(UserCreateDTO createDTO) {
    log.info("Creating new user with email: {}", createDTO.getEmail());

    // Check for duplicate email
    if (userRepository.existsByEmail(createDTO.getEmail())) {
      throw new DuplicateResourceException("Email already exists: " + createDTO.getEmail());
    }

    // Check for duplicate username
    if (userRepository.existsByName(createDTO.getName())) {
      throw new DuplicateResourceException("Username already exists: " + createDTO.getName());
    }

    User user = userMapper.toEntity(createDTO);
    user.setPassword(passwordEncoder.encode(createDTO.getPassword()));

    User savedUser = userRepository.save(user);
    log.info("Successfully created user with ID: {}", savedUser.getId());

    return userMapper.toResponseDTO(savedUser);
  }

  /**
   * Get user by ID
   */
  @Transactional(readOnly = true)
  public UserResponseDTO getUserById(Long id) {
    log.debug("Fetching user with ID: {}", id);

    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

    return userMapper.toResponseDTO(user);
  }

  /**
   * Get all users with pagination
   */
  @Transactional(readOnly = true)
  public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
    log.debug("Fetching all users with pagination: {}", pageable);

    Page<User> users = userRepository.findAll(pageable);
    return users.map(userMapper::toResponseDTO);
  }

  /**
   * Update user
   */
  public UserResponseDTO updateUser(Long id, UserUpdateDTO updateDTO) {
    log.info("Updating user with ID: {}", id);

    User existingUser = userRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

    // Check for duplicate email (excluding current user)
    if (
      updateDTO.getEmail() != null &&
        !updateDTO.getEmail().equals(existingUser.getEmail()) &&
      userRepository.existsByEmail(updateDTO.getEmail())
    ) {
      throw new DuplicateResourceException("Email already exists: " + updateDTO.getEmail());
    }

    // Check for duplicate username (excluding current user)
    if (updateDTO.getName() != null && !updateDTO.getName().equals(existingUser.getName())
        && userRepository.existsByName(updateDTO.getName())) {
      throw new DuplicateResourceException("Username already exists: " + updateDTO.getName());
    }

    // Update fields only if they are provided
    userMapper.updateEntityFromDTO(updateDTO, existingUser);

    // Encode password if provided
    if (updateDTO.getPassword() != null) {
      existingUser.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
    }

    User updatedUser = userRepository.save(existingUser);
    log.info("Successfully updated user with ID: {}", updatedUser.getId());

    return userMapper.toResponseDTO(updatedUser);
  }

  /**
   * Delete user by ID
   */
  public void deleteUser(Long id) {
    log.info("Deleting user with ID: {}", id);

    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("User not found with ID: " + id);
    }

    userRepository.deleteById(id);
    log.info("Successfully deleted user with ID: {}", id);
  }

  /**
   * Get user by email
   */
  @Transactional(readOnly = true)
  public UserResponseDTO getUserByEmail(String email) {
    log.debug("Fetching user with email: {}", email);

    User user = userRepository.findByEmail(email)
      .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

    return userMapper.toResponseDTO(user);
  }

  /**
   * Get user by username
   */
  @Transactional(readOnly = true)
  public UserResponseDTO getUserByUsername(String username) {
    log.debug("Fetching user with username: {}", username);

    User user = userRepository.findByName(username)
      .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

    return userMapper.toResponseDTO(user);
  }

  /**
   * Search users by search term
   */
  @Transactional(readOnly = true)
  public Page<UserResponseDTO> searchUsers(String searchTerm, Pageable pageable) {
    log.debug("Searching users with term: {}", searchTerm);

    Page<User> users = userRepository.findBySearchTerm(searchTerm, pageable);
    return users.map(userMapper::toResponseDTO);
  }

  /**
   * Get users by role
   */
  @Transactional(readOnly = true)
  public List<UserResponseDTO> getUsersByRole(UserRole role) {
    log.debug("Fetching users with role: {}", role);

    List<User> users = userRepository.findByRole(role);
    return users.stream()
      .map(userMapper::toResponseDTO)
      .toList();
  }

  /**
   * Get users by status
   */
  @Transactional(readOnly = true)
  public List<UserResponseDTO> getUsersByStatus(UserStatus status) {
    log.debug("Fetching users with status: {}", status);

    List<User> users = userRepository.findByStatus(status);
    return users.stream()
      .map(userMapper::toResponseDTO)
      .toList();
  }

  /**
   * Update user status
   */
  public UserResponseDTO updateUserStatus(Long id, UserStatus status) {
    log.info("Updating status for user with ID: {} to {}", id, status);

    User user = userRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

    user.setStatus(status);
    User updatedUser = userRepository.save(user);

    return userMapper.toResponseDTO(updatedUser);
  }
}