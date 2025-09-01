package com.slash.music.dto;

import com.slash.music.model.Genre;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class SongCreateRequest {
  @NotBlank
  private String title;
  @NotNull
  @Positive
  private Integer durationSeconds;
  @NotBlank
  private String fileUrl;
  @NotNull
  private Genre genre;
  @NotNull
  @Positive
  private Long artistId; // required
  @Positive
  private Long albumId; // optional
}
