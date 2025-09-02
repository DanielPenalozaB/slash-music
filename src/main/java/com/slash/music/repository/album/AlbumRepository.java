package com.slash.music.repository.album;

import com.slash.music.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    // Buscar álbumes por artista
    List<Album> findByArtistId(Long artistId);

    // Buscar álbumes por género
    List<Album> findByGenre(com.slash.music.model.Genre genre);

    // Buscar álbumes por título (similar a LIKE)
    List<Album> findByTitleContainingIgnoreCase(String title);
}

    

