package com.slash.music.dto;

import com.slash.music.model.Genre;
import jakarta.validation.constraints.*;

public class SongCreateRequest {
  @NotBlank public String title;
  @NotNull  @Positive public Integer durationSeconds;
  @NotBlank public String fileUrl;
  @NotNull  public Genre genre;
  @NotNull  @Positive public Long artistId;   // required
  @Positive public Long albumId;             // optional
}