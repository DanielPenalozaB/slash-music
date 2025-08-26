package com.slash.music.dto.artist;

import com.slash.music.model.Genre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * DTO for updating an artist
 * @author [Juan Camilo Gallego Riveros]
 * @github [https://github.com/0xcamix]
 * @version 1.0
 * @since [2025-08-25]
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistUpdateDTO {
    private String name;
    private String biography;
    private Genre genre;
    private String profileImageUrl;
    private Boolean verified;
}
