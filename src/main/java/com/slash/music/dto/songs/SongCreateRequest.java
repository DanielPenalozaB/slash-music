package com.slash.music.dto.songs;

import com.slash.music.model.Genre;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
