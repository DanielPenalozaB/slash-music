package com.slash.music.dto;

import java.time.LocalDateTime;

public class SongResponse {
    public Long id;
  public String title;
  public Integer duration;

  // Devuelve IDs; si luego quieres nombres, se pueden añadir
  public Long artistId;
  public Long albumId;
  public Long genreId;

  public LocalDateTime createdAt;
  public LocalDateTime updatedAt;
}
