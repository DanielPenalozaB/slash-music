package com.slash.music.repository;

import com.slash.music.model.User;
import com.slash.music.model.UserRole;
import com.slash.music.model.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by name
     */
    Optional<User> findByName(String name);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Check if name exists
     */
    boolean existsByName(String name);

    /**
     * Find users by role
     */
    List<User> findByRole(UserRole role);

    /**
     * Find users by status
     */
    List<User> findByStatus(UserStatus status);

    /**
     * Find users by role and status with pagination
     */
    Page<User> findByRoleAndStatus(UserRole role, UserStatus status, Pageable pageable);

    /**
     * Search users by name or email (case insensitive)
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find active users only
     */
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE'")
    List<User> findActiveUsers();

    /**
     * Count users by role
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    Long countByRole(@Param("role") UserRole role);

    /**
     * Find users with most playlists (for analytics)
     */
    @Query("SELECT u FROM User u LEFT JOIN u.playlists p GROUP BY u ORDER BY COUNT(p) DESC")
    List<User> findUsersOrderByPlaylistCountDesc(Pageable pageable);
}