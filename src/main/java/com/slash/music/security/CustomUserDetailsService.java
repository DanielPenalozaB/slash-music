package com.slash.music.security;

import com.slash.music.model.User;
import com.slash.music.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.debug("Loading user by username: {}", username);

    User user = userRepository.findByEmail(username)
      .orElseThrow(() -> {
        log.error("User not found with email: {}", username);
        return new UsernameNotFoundException("User not found with email: " + username);
      });

    log.debug("User found: {}, Status: {}", user.getEmail(), user.getStatus());
    return CustomUserDetails.create(user);
  }

  @Transactional(readOnly = true)
  public UserDetails loadUserById(Long id) {
    log.debug("Loading user by ID: {}", id);

    User user = userRepository.findById(id)
      .orElseThrow(() -> {
        log.error("User not found with ID: {}", id);
        return new UsernameNotFoundException("User not found with ID: " + id);
      });

    return CustomUserDetails.create(user);
  }
}