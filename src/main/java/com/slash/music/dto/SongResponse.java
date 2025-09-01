package com.slash.music.dto;

import com.slash.music.model.Genre;
import java.time.LocalDateTime;

public class SongResponse {
  public Long id;
  public String title;
  public Integer durationSeconds;
  public String fileUrl;
  public Genre genre;
  public Long playCount;

  public Long artistId;
  public String artistName;

  public Long albumId;
  public String albumTitle;

  public LocalDateTime createdAt;
  public LocalDateTime updatedAt;
}
