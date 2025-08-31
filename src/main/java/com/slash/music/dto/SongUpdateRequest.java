package com.slash.music.dto;
import jakarta.validation.constraints.*;

public class SongUpdateRequest {
    public String title;
  @Positive public Integer duration;
  public Long artistId;
  public Long albumId;
  public Long genreId;
}
