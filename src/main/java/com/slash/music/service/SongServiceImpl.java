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
    r.setId(s.getId());
    r.setTitle(s.getTitle());
    r.setDurationSeconds(s.getDurationSeconds());
    r.setFileUrl(s.getFileUrl());
    r.setGenre(s.getGenre());
    r.setPlayCount(s.getPlayCount());
    if (s.getArtist() != null) {
      r.setArtistId(s.getArtist().getId());
      r.setArtistName(s.getArtist().getName());
    }
    if (s.getAlbum() != null) {
      r.setAlbumId(s.getAlbum().getId());
      r.setAlbumTitle(s.getAlbum().getTitle());
    }
    r.setCreatedAt(s.getCreatedAt());
    r.setUpdatedAt(s.getUpdatedAt());
    return r;
  }

  @Override
  public SongResponse create(SongCreateRequest req) {
    var artist = artistRepository.findById(req.getArtistId())
        // usa la firma real de tu excepción:
        .orElseThrow(
            () -> new ResourceNotFoundException("Artist not found with id " + req.getArtistId()));
    // .orElseThrow(() -> new
    // ResourceNotFoundException("Artist","id",req.artistId));

    Album album = null;
    if (req.getAlbumId() != null) {
      album = albumRepository.findById(req.getAlbumId()).orElseThrow(
          () -> new ResourceNotFoundException("Album not found with id " + req.getAlbumId()));
      // .orElseThrow(() -> new ResourceNotFoundException("Album","id",req.albumId));
    }
    var s = new Song();
    s.setTitle(req.getTitle());
    s.setDurationSeconds(req.getDurationSeconds());
    s.setFileUrl(req.getFileUrl());
    s.setGenre(req.getGenre());
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

    if (req.getTitle() != null) {
      s.setTitle(req.getTitle());
    }
    if (req.getDurationSeconds() != null) {
      s.setDurationSeconds(req.getDurationSeconds());
    }
    if (req.getFileUrl() != null) {
      s.setFileUrl(req.getFileUrl());
    }
    if (req.getGenre() != null) {
      s.setGenre(req.getGenre());
    }

    if (req.getArtistId() != null) {
      var artist = artistRepository.findById(req.getArtistId()).orElseThrow(
          () -> new ResourceNotFoundException("Artist not found with id " + req.getArtistId()));
      s.setArtist(artist);
    }
    if (req.getAlbumId() != null) {
      var album = albumRepository.findById(req.getAlbumId()).orElseThrow(
          () -> new ResourceNotFoundException("Album not found with id " + req.getAlbumId()));
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
