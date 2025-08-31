package com.slash.music.controller.artist;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.slash.music.dto.artist.ArtistCreateDTO;
import com.slash.music.dto.artist.ArtistResponseDTO;
import com.slash.music.dto.artist.ArtistUpdateDTO;
import com.slash.music.exception.ErrorResponse;
import com.slash.music.service.artist.ArtistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * Controller for managing artists
 * @author [Juan Camilo Gallego Riveros]
 * @github [https://github.com/0xcamix]
 * @version 1.0
 * @since [2025-08-25]
 */
@RestController
@RequestMapping("/api/v1/artists")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Artist", description = "Artist API")
public class ArtistController {

  private final ArtistService artistService;

  /**
   * Create a new artist
   * @param createDTO ArtistCreateDTO
   * @return ArtistResponseDTO
   * @message [201] Artist created successfully
   * @message [400] Invalid request body
   * @message [409] Artist already exists
   */
  @Operation(summary = "Create a new artist", description = "Creates a new artist")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Artist created successfully",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ArtistResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Invalid request body",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "409", description = "Artist already exists",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping("/create")
  public ResponseEntity<ArtistResponseDTO> createArtist(@RequestBody ArtistCreateDTO createDTO) {
    log.info("Creating new artist with name: {}", createDTO.getName());
    ArtistResponseDTO response = artistService.createArtist(createDTO);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  /**
   * Get an artist by ID
   * @param id Artist ID
   * @return ArtistResponseDTO
   * @message [200] Artist retrieved successfully
   * @message [404] Artist not found
   */
  @Operation(summary = "Get an artist by ID", description = "Retrieves an artist by their unique identifier")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Artist retrieved successfully",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ArtistResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Artist not found",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/{id}")
  public ResponseEntity<ArtistResponseDTO> getArtistById(@PathVariable Long id) {
    log.info("Fetching artist with ID: {}", id);
    ArtistResponseDTO response = artistService.getArtistById(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * Get all artists
   * @param page Page number (0-based)
   * @param size Page size
   * @param sortBy Field to sort by
   * @param sortDirection Sort direction (asc or desc)
   * @return Page<ArtistResponseDTO>
   * @message [200] Artists retrieved successfully
   * @message [404] Artists not found
   */
  @Operation(summary = "Get all artists", description = "Retrieves all artists with pagination")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Artists retrieved successfully",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ArtistResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Artists not found",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/all")
  public ResponseEntity<Page<ArtistResponseDTO>> getAllArtists(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDirection) {

    log.info("Fetching all artists - page: {}, size: {}, sortBy: {}, direction: {}",
        page, size, sortBy, sortDirection);

    Sort sort = sortDirection.equalsIgnoreCase("desc")
        ? Sort.by(sortBy).descending()
        : Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<ArtistResponseDTO> response = artistService.getAllArtists(pageable);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * Update an artist
   * @param id Artist ID
   * @param updateDTO ArtistUpdateDTO
   * @return ArtistResponseDTO
   * @message [200] Artist updated successfully
   * @message [404] Artist not found
   */
  @Operation(summary = "Update an artist", description = "Updates an artist by their unique identifier")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Artist updated successfully",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ArtistResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Artist not found",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PutMapping("/{id}")
  public ResponseEntity<ArtistResponseDTO> updateArtist(@PathVariable Long id,
      @RequestBody ArtistUpdateDTO updateDTO) {
    log.info("Updating artist with ID: {}", id);
    ArtistResponseDTO response = artistService.updateArtist(id, updateDTO);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * Delete an artist
   * @param id Artist ID
   * @return Void
   * @message [200] Artist deleted successfully
   * @message [404] Artist not found
   */
  @Operation(summary = "Delete an artist", description = "Deletes an artist by their unique identifier")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Artist deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Artist not found",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
    log.info("Deleting artist with ID: {}", id);
    artistService.deleteArtist(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Get an artist by name
   * @param name Artist name
   * @return ArtistResponseDTO
   * @message [200] Artist retrieved successfully
   * @message [404] Artist not found
   */
  @Operation(summary = "Get an artist by name", description = "Retrieves an artist by their name")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Artist retrieved successfully",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ArtistResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Artist not found",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/name/{name}")
  public ResponseEntity<ArtistResponseDTO> getArtistByName(@PathVariable String name) {
    log.info("Fetching artist with name: {}", name);
    ArtistResponseDTO response = artistService.getArtistByName(name);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * Get an artist by search term
   * @param name Artist name
   * @return List<ArtistResponseDTO>
   * @message [200] Artist retrieved successfully
   * @message [404] Artist not found
   */
  @Operation(summary = "Get an artist by search term", description = "Retrieves an artist by their search term")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Artist retrieved successfully",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ArtistResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Artist not found",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/search/{name}")
  public ResponseEntity<List<ArtistResponseDTO>> getArtistBySearchTerm(@PathVariable String name) {
    log.info("Fetching artist with search term: {}", name);
    List<ArtistResponseDTO> response = artistService.getArtistBySearchTerm(name);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
