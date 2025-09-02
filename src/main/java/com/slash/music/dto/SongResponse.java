package com.slash.music.dto;

import com.slash.music.model.Genre;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongResponse {
  private Long id;
  private String title;
  private Integer durationSeconds;
  private String fileUrl;
  private Genre genre;
  private Long playCount;

  private Long artistId;
  private String artistName;

  private Long albumId;
  private String albumTitle;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
