package com.slash.music.repository.artist;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slash.music.model.Artist;

/*
 * Repository for managing artists
 * @author [Juan Camilo Gallego Riveros]
 * @version 1.0
 * @since [2025-08-25]
 */
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    /**
     * Check if artist exists by name
     * @param name Artist name
     * @return true if artist exists, false otherwise
     */
  boolean existsByName(String name);

  /**
   * Find artist by name
   * @param name Artist name
   * @return Artist
   */
  Optional<Artist> findByName(String name);

  /**
   * Find a list of artist by similar name
   * @param name Artist name
   * @return List<Artist>
   */
  List<Artist> findByNameContaining(String name);
}
