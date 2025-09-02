package com.slash.music.controller.album;

import com.slash.music.model.Album;
import com.slash.music.service.album.AlbumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/albums")
public class AlbumController {

  private final AlbumService albumService;

  public AlbumController(AlbumService albumService) {
    this.albumService = albumService;
  }

  // Obtener todos los álbumes
  @GetMapping
  public List<Album> getAllAlbums() {
    return albumService.getAllAlbums();
  }

  // Obtener un álbum por id
  @GetMapping("/{id}")
  public ResponseEntity<Album> getAlbumById(@PathVariable Long id) {
    return albumService.getAlbumById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  // Obtener álbumes de un artista específico
  @GetMapping("/artist/{artistId}")
  public List<Album> getAlbumsByArtist(@PathVariable Long artistId) {
    return albumService.getAlbumsByArtist(artistId);
  }

  // Buscar álbumes por título
  @GetMapping("/search")
  public List<Album> searchAlbums(@RequestParam String title) {
    return albumService.searchAlbumsByTitle(title);
  }

  // Crear un álbum
  @PostMapping
  public Album createAlbum(@RequestBody Album album) {
    return albumService.createAlbum(album);
  }

  // Actualizar un álbum
  @PutMapping("/{id}")
  public ResponseEntity<Album> updateAlbum(@PathVariable Long id, @RequestBody Album album) {
    try {
      return ResponseEntity.ok(albumService.updateAlbum(id, album));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // Eliminar un álbum
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
    albumService.deleteAlbum(id);
    return ResponseEntity.noContent().build();
  }
}
