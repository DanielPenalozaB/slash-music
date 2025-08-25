package com.slash.music.dto;

import com.slash.music.model.UserRole;
import com.slash.music.model.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
  @Email(message = "Email should be valid")
  @Size(max = 100, message = "Email must be less than 100 characters")
  private String email;

  @Size(max = 50, message = "Name must be less than 50 characters")
  private String name;

  @Size(min = 6, message = "Password must be at least 6 characters")
  private String password;

  private UserRole role;

  private UserStatus status;

  private String profileImageUrl;
}