package com.slash.music.dto.artist;

import com.slash.music.model.Genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/*
 * DTO for creating an artist
 * @author [Juan Camilo Gallego Riveros]
 * @github [https://github.com/0xcamix]
 * @version 1.0
 * @since [2025-08-25]
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistCreateDTO {
    @NotBlank(message = "Name is required")
    @Size(max = 30, message = "Name must be less than 30 characters")
    private String name;

    @NotBlank(message = "Biography is required")
    @Size(max = 200, message = "Biography must be less than 200 characters")
    private String biography;

    @NotNull(message = "Genre is required")
    private Genre genre;

    private String profileImageUrl;

    private Boolean verified = false;
}
