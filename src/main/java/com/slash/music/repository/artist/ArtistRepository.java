package com.slash.music.repository.artist;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slash.music.model.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

}
