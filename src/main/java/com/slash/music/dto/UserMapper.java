package com.slash.music.dto;

import com.slash.music.model.User;
import org.springframework.stereotype.Component;

/**
 * Mapper class to convert between User entities and DTOs
 * This is a manual mapper - you could also use MapStruct for more complex
 * scenarios
 */
@Component
public class UserMapper {
  /**
   * Convert UserCreateDTO to User entity
   */
  public User toEntity(UserCreateDTO dto) {
    if (dto == null) {
      return null;
    }

    User user = new User();
    user.setEmail(dto.getEmail());
    user.setName(dto.getName());
    user.setRole(dto.getRole());
    user.setStatus(dto.getStatus());

    return user;
  }

  /**
   * Convert User entity to UserResponseDTO
   */
  public UserResponseDTO toResponseDTO(User user) {
    if (user == null) {
      return null;
    }

    UserResponseDTO dto = new UserResponseDTO();
    dto.setId(user.getId());
    dto.setEmail(user.getEmail());
    dto.setName(user.getName());
    dto.setRole(user.getRole());
    dto.setStatus(user.getStatus());
    dto.setCreatedAt(user.getCreatedAt());
    dto.setUpdatedAt(user.getUpdatedAt());

    return dto;
  }

  /**
   * Update existing User entity from UserUpdateDTO
   * Only updates fields that are not null in the DTO
   */
  public void updateEntityFromDTO(UserUpdateDTO dto, User user) {
    if (dto == null || user == null) {
      return;
    }

    if (dto.getEmail() != null) {
      user.setEmail(dto.getEmail());
    }
    if (dto.getName() != null) {
      user.setName(dto.getName());
    }
    if (dto.getRole() != null) {
      user.setRole(dto.getRole());
    }
    if (dto.getStatus() != null) {
      user.setStatus(dto.getStatus());
    }
    // Note: password update is handled separately in service layer
  }
}