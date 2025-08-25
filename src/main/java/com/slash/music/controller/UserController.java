package com.slash.music.controller;

import com.slash.music.dto.UserCreateDTO;
import com.slash.music.dto.UserResponseDTO;
import com.slash.music.dto.UserUpdateDTO;
import com.slash.music.model.UserRole;
import com.slash.music.model.UserStatus;
import com.slash.music.security.CustomUserDetails;
import com.slash.music.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "APIs for managing users in the Spotify-like application")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
  private final UserService userService;

  @Operation(summary = "Create a new user", description = "Creates a new user in the system (Admin only)")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "User created successfully",
      content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = UserResponseDTO.class)
        )),
    @ApiResponse(responseCode = "400", description = "Invalid input data"),
    @ApiResponse(responseCode = "403", description = "Access denied - Admin only"),
    @ApiResponse(responseCode = "409", description = "Email or username already exists")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO createDTO) {

    log.info("Creating new user with email: {}", createDTO.getEmail());
    UserResponseDTO createdUser = userService.createUser(createDTO);
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }

  @Operation(summary = "Get current user", description = "Retrieves the current authenticated user")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "User found",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = UserResponseDTO.class)
        )),
    @ApiResponse(responseCode = "401", description = "User not authenticated")
  })
  @GetMapping("/me")
  public ResponseEntity<UserResponseDTO> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

    log.debug("Fetching current user: {}", userDetails.getEmail());
    UserResponseDTO user = userService.getUserById(userDetails.getId());
    return ResponseEntity.ok(user);
  }

  @Operation(summary = "Update current user", description = "Updates the current authenticated user's information")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "User updated successfully",
      content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = UserResponseDTO.class)
        )),
    @ApiResponse(responseCode = "400", description = "Invalid input data"),
    @ApiResponse(responseCode = "401", description = "User not authenticated"),
    @ApiResponse(responseCode = "409", description = "Email or username already exists")
  })
  @PutMapping("/me")
  public ResponseEntity<UserResponseDTO> updateCurrentUser(@Valid @RequestBody UserUpdateDTO updateDTO) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

    log.info("Updating current user: {}", userDetails.getEmail());
    UserResponseDTO updatedUser = userService.updateUser(userDetails.getId(), updateDTO);
    return ResponseEntity.ok(updatedUser);
  }

  @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique identifier (Admin only)")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "User found",
      content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = UserResponseDTO.class)
        )),
    @ApiResponse(responseCode = "403", description = "Access denied - Admin only"),
    @ApiResponse(responseCode = "404", description = "User not found")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getUserById(
    @Parameter(description = "User ID", required = true) @PathVariable Long id
  ) {
    log.debug("Fetching user with ID: {}", id);
    UserResponseDTO user = userService.getUserById(id);
    return ResponseEntity.ok(user);
  }

  @Operation(summary = "Get all users", description = "Retrieves all users with pagination and sorting (Admin only)")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
    @ApiResponse(responseCode = "403", description = "Access denied - Admin only")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
    @Parameter(description = "Page number (0-based)", example = "0")
    @RequestParam(defaultValue = "0") int page,
    @Parameter(description = "Page size", example = "10")
    @RequestParam(defaultValue = "10") int size,
    @Parameter(description = "Sort by field", example = "createdAt")
    @RequestParam(defaultValue = "createdAt") String sortBy,
    @Parameter(description = "Sort direction", example = "desc")
    @RequestParam(defaultValue = "desc") String sortDir
  ) {
    log.debug("Fetching all users - page: {}, size: {}, sortBy: {}, sortDir: {}",
      page, size, sortBy, sortDir);

    Sort sort = sortDir.equalsIgnoreCase("desc")
      ? Sort.by(sortBy).descending()
      : Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<UserResponseDTO> users = userService.getAllUsers(pageable);

    return ResponseEntity.ok(users);
  }

  @Operation(summary = "Update user", description = "Updates an existing user's information (Admin only)")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "User updated successfully",
      content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = UserResponseDTO.class)
        )),
    @ApiResponse(responseCode = "403", description = "Access denied - Admin only"),
    @ApiResponse(responseCode = "404", description = "User not found"),
    @ApiResponse(responseCode = "400", description = "Invalid input data"),
    @ApiResponse(responseCode = "409", description = "Email or username already exists")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDTO> updateUser(
    @Parameter(description = "User ID", required = true) @PathVariable Long id,
    @Valid @RequestBody UserUpdateDTO updateDTO
  ) {
    log.info("Updating user with ID: {}", id);
    UserResponseDTO updatedUser = userService.updateUser(id, updateDTO);
    return ResponseEntity.ok(updatedUser);
  }

  @Operation(summary = "Delete user", description = "Deletes a user from the system (Admin only)")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "User deleted successfully"),
    @ApiResponse(responseCode = "403", description = "Access denied - Admin only"),
    @ApiResponse(responseCode = "404", description = "User not found")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(
    @Parameter(description = "User ID", required = true) @PathVariable Long id
  ) {
    log.info("Deleting user with ID: {}", id);
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Search users", description = "Searches users by name, username, or email")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
  })
  @GetMapping("/search")
  public ResponseEntity<Page<UserResponseDTO>> searchUsers(
    @Parameter(description = "Search term", required = true) @RequestParam String q,
    @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
    @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size
  ) {
    log.debug("Searching users with term: {}", q);

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<UserResponseDTO> users = userService.searchUsers(q, pageable);

    return ResponseEntity.ok(users);
  }

  @Operation(summary = "Get user by email", description = "Retrieves a user by their email address (Admin only)")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "User found",
      content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = UserResponseDTO.class)
      )),
    @ApiResponse(responseCode = "403", description = "Access denied - Admin only"),
    @ApiResponse(responseCode = "404", description = "User not found")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/email/{email}")
  public ResponseEntity<UserResponseDTO> getUserByEmail(
    @Parameter(description = "User email", required = true) @PathVariable String email
  ) {
    log.debug("Fetching user with email: {}", email);
    UserResponseDTO user = userService.getUserByEmail(email);
    return ResponseEntity.ok(user);
  }

  @Operation(summary = "Get user by username", description = "Retrieves a user by their username (Admin only)")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "User found",
      content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = UserResponseDTO.class)
        )),
    @ApiResponse(responseCode = "403", description = "Access denied - Admin only"),
    @ApiResponse(responseCode = "404", description = "User not found")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/username/{username}")
  public ResponseEntity<UserResponseDTO> getUserByUsername(
    @Parameter(description = "Username", required = true) @PathVariable String username
  ) {
    log.debug("Fetching user with username: {}", username);
    UserResponseDTO user = userService.getUserByUsername(username);
    return ResponseEntity.ok(user);
  }

  @Operation(summary = "Get users by role", description = "Retrieves all users with a specific role (Admin only)")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
    @ApiResponse(responseCode = "403", description = "Access denied - Admin only")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/role/{role}")
  public ResponseEntity<List<UserResponseDTO>> getUsersByRole(
    @Parameter(description = "User role", required = true) @PathVariable UserRole role
  ) {
    log.debug("Fetching users with role: {}", role);
    List<UserResponseDTO> users = userService.getUsersByRole(role);
    return ResponseEntity.ok(users);
  }

  @Operation(summary = "Get users by status", description = "Retrieves all users with a specific status (Admin only)")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
    @ApiResponse(responseCode = "403", description = "Access denied - Admin only")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/status/{status}")
  public ResponseEntity<List<UserResponseDTO>> getUsersByStatus(
    @Parameter(description = "User status", required = true) @PathVariable UserStatus status
  ) {
    log.debug("Fetching users with status: {}", status);
    List<UserResponseDTO> users = userService.getUsersByStatus(status);
    return ResponseEntity.ok(users);
  }

  @Operation(
    summary = "Update user status",
    description = "Updates a user's status (activate, deactivate, ban, etc.) (Admin only)"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "User status updated successfully",
      content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = UserResponseDTO.class)
        )),
    @ApiResponse(responseCode = "403", description = "Access denied - Admin only"),
    @ApiResponse(responseCode = "404", description = "User not found")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/{id}/status")
  public ResponseEntity<UserResponseDTO> updateUserStatus(
    @Parameter(description = "User ID", required = true) @PathVariable Long id,
    @Parameter(description = "New user status", required = true) @RequestParam UserStatus status
  ) {
    log.info("Updating status for user ID {} to {}", id, status);
    UserResponseDTO updatedUser = userService.updateUserStatus(id, status);
    return ResponseEntity.ok(updatedUser);
  }
}