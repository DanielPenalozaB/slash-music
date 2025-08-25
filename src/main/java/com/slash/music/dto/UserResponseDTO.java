package com.slash.music.dto;

import com.slash.music.model.UserRole;
import com.slash.music.model.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
  private Long id;
  private String email;
  private String name;
  private UserRole role;
  private UserStatus status;
  private String profileImageUrl;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}