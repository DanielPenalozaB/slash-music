package com.slash.music.service.album;

import com.slash.music.model.Album;
import com.slash.music.repository.album.AlbumRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un servicio de Spring, lo que permite inyectarla en
         // otros componentes
public class AlbumService {

  // Repositorio que permite acceder a la base de datos de álbumes
  private final AlbumRepository albumRepository;

  // Constructor con inyección de dependencias de Spring
  public AlbumService(AlbumRepository albumRepository) {
    this.albumRepository = albumRepository;
  }

  // Obtiene todos los álbumes de la base de datos
  public List<Album> getAllAlbums() {
    return albumRepository.findAll();
  }

  // Busca un álbum por su ID, devuelve un Optional porque puede o no existir
  public Optional<Album> getAlbumById(Long id) {
    return albumRepository.findById(id);
  }

  // Obtiene todos los álbumes asociados a un artista específico
  public List<Album> getAlbumsByArtist(Long artistId) {
    return albumRepository.findByArtistId(artistId);
  }

  // Busca álbumes cuyo título contenga cierto texto (ignorando
  // mayúsculas/minúsculas)
  public List<Album> searchAlbumsByTitle(String title) {
    return albumRepository.findByTitleContainingIgnoreCase(title);
  }

  // Crea y guarda un nuevo álbum en la base de datos
  public Album createAlbum(Album album) {
    return albumRepository.save(album);
  }

  // Actualiza un álbum existente con los nuevos datos
  public Album updateAlbum(Long id, Album albumDetails) {
    return albumRepository.findById(id).map(album -> {
      // Se actualizan los campos del álbum encontrado
      album.setTitle(albumDetails.getTitle());
      album.setDescription(albumDetails.getDescription());
      album.setReleaseDate(albumDetails.getReleaseDate());
      album.setGenre(albumDetails.getGenre());
      album.setArtist(albumDetails.getArtist());
      // Se guarda el álbum actualizado
      return albumRepository.save(album);
    }).orElseThrow(() -> new RuntimeException("Álbum no encontrado con id " + id));
  }

  // Elimina un álbum de la base de datos por su ID
  public void deleteAlbum(Long id) {
    albumRepository.deleteById(id);
  }
}
