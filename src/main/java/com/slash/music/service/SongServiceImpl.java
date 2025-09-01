package com.slash.music.service;

import com.slash.music.dto.*;
import com.slash.music.exception.ResourceNotFoundException;
import com.slash.music.model.*;
import com.slash.music.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link SongService}. Keeps comments in English (per code review).
 * Provides CRUD + search with pagination.
 *
 * Author: Miguel David Palencia
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SongServiceImpl implements SongService {

  private final SongRepository songRepository;
  private final ArtistRepository artistRepository;
  private final AlbumRepository albumRepository;

  private static SongResponse toResponse(Song s) {
    var r = new SongResponse();
    r.id = s.getId();
    r.title = s.getTitle();
    r.durationSeconds = s.getDurationSeconds();
    r.fileUrl = s.getFileUrl();
    r.genre = s.getGenre();
    r.playCount = s.getPlayCount();
    if (s.getArtist() != null) {
      r.artistId = s.getArtist().getId();
      r.artistName = s.getArtist().getName();
    }
    if (s.getAlbum() != null) {
      r.albumId = s.getAlbum().getId();
      r.albumTitle = s.getAlbum().getTitle();
    }
    r.createdAt = s.getCreatedAt();
    r.updatedAt = s.getUpdatedAt();
    return r;
  }

  @Override
  public SongResponse create(SongCreateRequest req) {
    var artist = artistRepository.findById(req.artistId)
        // usa la firma real de tu excepción:
        .orElseThrow(
            () -> new ResourceNotFoundException("Artist not found with id " + req.artistId));
    // .orElseThrow(() -> new
    // ResourceNotFoundException("Artist","id",req.artistId));

    Album album = null;
    if (req.albumId != null) {
      album = albumRepository.findById(req.albumId).orElseThrow(
          () -> new ResourceNotFoundException("Album not found with id " + req.albumId));
      // .orElseThrow(() -> new ResourceNotFoundException("Album","id",req.albumId));
    }
    var s = new Song();
    s.setTitle(req.title);
    s.setDurationSeconds(req.durationSeconds);
    s.setFileUrl(req.fileUrl);
    s.setGenre(req.genre);
    s.setArtist(artist);
    s.setAlbum(album);

    return toResponse(songRepository.save(s));
  }

  @Override
  @Transactional(readOnly = true)
  public SongResponse findById(Long id) {
    var s = songRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Song not found with id " + id));
    // .orElseThrow(() -> new ResourceNotFoundException("Song","id",id));
    return toResponse(s);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<SongResponse> search(String title, String artist, int page, int size, String sort) {
    var srt = Sort.by((sort == null || sort.isBlank()) ? "id" : sort).ascending();
    var pageable = PageRequest.of(page, size, srt);
    Page<Song> base = (title != null && !title.isBlank())
        ? songRepository.findByTitleContainingIgnoreCase(title, pageable)
        : (artist != null && !artist.isBlank())
            ? songRepository.findByArtistNameContainingIgnoreCase(artist, pageable)
            : songRepository.findAll(pageable);
    return base.map(SongServiceImpl::toResponse);
  }

  @Override
  public SongResponse update(Long id, SongUpdateRequest req) {
    var s = songRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Song not found with id " + id));
    // .orElseThrow(() -> new ResourceNotFoundException("Song","id",id));

    if (req.title != null) {
      s.setTitle(req.title);
    }
    if (req.durationSeconds != null) {
      s.setDurationSeconds(req.durationSeconds);
    }
    if (req.fileUrl != null) {
      s.setFileUrl(req.fileUrl);
    }
    if (req.genre != null) {
      s.setGenre(req.genre);
    }

    if (req.artistId != null) {
      var artist = artistRepository.findById(req.artistId).orElseThrow(
          () -> new ResourceNotFoundException("Artist not found with id " + req.artistId));
      s.setArtist(artist);
    }
    if (req.albumId != null) {
      var album = albumRepository.findById(req.albumId).orElseThrow(
          () -> new ResourceNotFoundException("Album not found with id " + req.albumId));
      s.setAlbum(album);
    }

    return toResponse(songRepository.save(s));
  }

  @Override
  public void delete(Long id) {
    if (!songRepository.existsById(id)) {
      throw new ResourceNotFoundException("Song not found with id " + id);
      // throw new ResourceNotFoundException("Song","id",id);
    }
    songRepository.deleteById(id);
  }
}
