package com.slash.music.dto.artist;

import org.springframework.stereotype.Component;

import com.slash.music.model.Artist;


/*
 * Mapper for Artist entity and DTOs
 * 
 * @author [Juan Camilo Gallego Riveros]
 * @github [https://github.com/0xcamix]
 * @version 1.0
 * @since [2025-08-25]
 */
@Component
public class ArtistMapper {

    /**
     * Convert ArtistCreateDTO to Artist entity
     * @param dto ArtistCreateDTO
     * @return Artist entity
     */
  public Artist toEntity(ArtistCreateDTO dto) {
    if (dto == null) {
      return null;
    }

    Artist artist = new Artist();
    artist.setName(dto.getName());
    artist.setBiography(dto.getBiography());
    artist.setGenre(dto.getGenre());
    artist.setProfileImageUrl(dto.getProfileImageUrl());
    artist.setVerified(dto.getVerified());
    return artist;
  }

    /**
     * Convert Artist entity to ArtistResponseDTO
     * @param artist Artist entity
     * @return ArtistResponseDTO
     */
  public ArtistResponseDTO toResponseDTO(Artist artist) {
    if (artist == null) {
      return null;
    }

    ArtistResponseDTO dto = new ArtistResponseDTO();

    dto.setId(artist.getId());
    dto.setName(artist.getName());
    dto.setBiography(artist.getBiography());
    dto.setGenre(artist.getGenre());
    dto.setProfileImageUrl(artist.getProfileImageUrl());
    dto.setVerified(artist.getVerified());
    return dto;
  }

    /**
     * Update Artist entity from ArtistUpdateDTO
     * @param dto ArtistUpdateDTO
     * @param artist Artist entity
     */
  public void updateEntityFromDTO(ArtistUpdateDTO dto, Artist artist) {
    if (dto == null || artist == null) {
      return;
    }

    if (dto.getName() != null) {
      artist.setName(dto.getName());
    }

    if (dto.getBiography() != null) {
      artist.setBiography(dto.getBiography());
    }

    if (dto.getGenre() != null) {
      artist.setGenre(dto.getGenre());
    }

    if (dto.getProfileImageUrl() != null) {
      artist.setProfileImageUrl(dto.getProfileImageUrl());
    }
        
    if (dto.getVerified() != null) {
      artist.setVerified(dto.getVerified());
    }
  }
}
