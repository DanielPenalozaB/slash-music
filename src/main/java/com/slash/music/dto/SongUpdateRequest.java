package com.slash.music.dto;

import com.slash.music.model.Genre;
import jakarta.validation.constraints.*;

public class SongUpdateRequest {
  public String title;
  @Positive public Integer durationSeconds;
  public String fileUrl;
  public Genre genre;
  @Positive public Long artistId;
  @Positive public Long albumId;
}
