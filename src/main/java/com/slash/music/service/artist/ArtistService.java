package com.slash.music.service.artist;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.slash.music.dto.artist.ArtistCreateDTO;
import com.slash.music.dto.artist.ArtistMapper;
import com.slash.music.dto.artist.ArtistResponseDTO;
import com.slash.music.dto.artist.ArtistUpdateDTO;
import com.slash.music.exception.DuplicateResourceException;
import com.slash.music.exception.ResourceNotFoundException;
import com.slash.music.model.Artist;
import com.slash.music.repository.artist.ArtistRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * Service for managing artists
 * @author [Juan Camilo Gallego Riveros]
 * @version 1.0
 * @github [https://github.com/0xcamix]
 * @since [2025-08-25]
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ArtistService {

  private final ArtistRepository artistRepository;
  private final ArtistMapper artistMapper;

  /**
   * Create a new artist
   * @param createDTO ArtistCreateDTO
   * @return ArtistResponseDTO
   */
  public ArtistResponseDTO createArtist(ArtistCreateDTO createDTO) {
    log.info("Creating new artist with name: {}", createDTO.getName());

    if (artistRepository.existsByName(createDTO.getName())) {
      throw new DuplicateResourceException("Artist already exists: " + createDTO.getName());
    }

    Artist artist = artistMapper.toEntity(createDTO);
    Artist savedArtist = artistRepository.save(artist);
    return artistMapper.toResponseDTO(savedArtist);
  }

  /**
   * Get artist by ID
   * @param id Artist ID
   * @return ArtistResponseDTO
   */
  public ArtistResponseDTO getArtistById(Long id) {
    log.debug("Fetching artist with ID: {}", id);

    Artist artist = artistRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Artist not found with ID: " + id));

    return artistMapper.toResponseDTO(artist);
  }

  /**
   * Get all artists with pagination
   * @param pageable Pageable
   * @return Page<ArtistResponseDTO>
   */
  @Transactional(readOnly = true)
  public Page<ArtistResponseDTO> getAllArtists(Pageable pageable) {
    log.debug("Fetching all artists with pagination: {}", pageable);

    Page<Artist> artists = artistRepository.findAll(pageable);
    return artists.map(artistMapper::toResponseDTO);
  }

  /**
   * Update an artist
   * @param id Artist ID
   * @param updateDTO ArtistUpdateDTO
   * @return ArtistResponseDTO
   */
  public ArtistResponseDTO updateArtist(Long id, ArtistUpdateDTO updateDTO) {
    log.debug("Updating artist with ID: {}", id);

    Artist artist = artistRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Artist not found with ID: " + id));

    artistMapper.updateEntityFromDTO(updateDTO, artist);
    Artist updatedArtist = artistRepository.save(artist);
    return artistMapper.toResponseDTO(updatedArtist);
  }

  /**
   * Delete an artist
   * @param id Artist ID
   */
    log.debug("Deleting artist with ID: {}", id);
    Artist artist = artistRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Artist not found with ID: " + id));
    artistRepository.delete(artist);
  }

  /**
   * Get artist by name
   * @param name Artist name
   * @return ArtistResponseDTO
   */
  public ArtistResponseDTO getArtistByName(String name) {
    log.debug("Fetching artist with name: {}", name);

    Artist artist = artistRepository.findByName(name)
        .orElseThrow(() -> new ResourceNotFoundException("Artist not found with name: " + name));

    return artistMapper.toResponseDTO(artist);
  }

  /**
   * Get artist by search term by name
   * @param name Artist name
   * @return ArtistResponseDTO
   */
  public List<ArtistResponseDTO> getArtistBySearchTerm(String name) {
    log.debug("Fetching artist with search term: {}", name);

    List<Artist> artists = artistRepository.findByNameContaining(name);
    return artists.stream()
        .map(artistMapper::toResponseDTO)
        .collect(Collectors.toList());
  }
}
