package com.slash.music.security;

import com.slash.music.model.User;
import com.slash.music.model.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
  private Long id;
  private String email;
  private String name;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;
  private boolean accountNonExpired;
  private boolean accountNonLocked;
  private boolean credentialsNonExpired;
  private boolean enabled;

  public static CustomUserDetails create(User user) {
    Collection<GrantedAuthority> authorities = Collections.singleton(
      new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

    return new CustomUserDetails(
      user.getId(),
      user.getEmail(),
      user.getName(),
      user.getPassword(),
      authorities,
      true, // accountNonExpired
      user.getStatus() != UserStatus.BANNED, // accountNonLocked
      true, // credentialsNonExpired
      user.getStatus() == UserStatus.ACTIVE // enabled
    );
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}