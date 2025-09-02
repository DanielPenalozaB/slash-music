package com.slash.music.dto;


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
public class SongUpdateRequest {
  private String title;
  @Positive
  private Integer durationSeconds;
  private String fileUrl;
  private Genre genre;
  @Positive
  private Long artistId;
  @Positive
  private Long albumId;
}
