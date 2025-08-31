package com.slash.music.dto;
import jakarta.validation.constraints.*;

public class SongCreateRequest {
     @NotBlank public String title;
  @NotNull @Positive public Integer duration; // segundos

  // Relación por IDs (ajusta si en tu modelo usas otro tipo)
  public Long artistId;   // opcional u obligatorio según tu entidad
  public Long albumId;    // opcional
  public Long genreId;    // opcional
}
