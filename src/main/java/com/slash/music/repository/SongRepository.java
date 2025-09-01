package com.slash.music.repository;

import com.slash.music.model.Song;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
  Page<Song> findByTitleContainingIgnoreCase(String title, Pageable pageable);
  Page<Song> findByArtist_NameContainingIgnoreCase(String name, Pageable pageable);
}