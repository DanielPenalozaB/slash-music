package com.slash.music.controller;


import com.slash.music.dto.SongCreateRequest;
import com.slash.music.dto.SongUpdateRequest;
import com.slash.music.dto.SongResponse;
import com.slash.music.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Songs", description = "Endpoints para gestionar canciones")
@SecurityRequirement(name = "bearerAuth") // <- Protege todos los endpoints con esquema bearer (JWT)
                                          // en Swagger
@RestController
@RequestMapping("/api/v1/songs")

public class SongController {

  /**
   * REST controller exposing CRUD and search endpoints for Songs. Swagger security is documented
   * via @SecurityRequirement; see OpenApiConfig for bearer scheme.
   *
   * Endpoints: - POST /api/v1/songs - GET /api/v1/songs - GET /api/v1/songs/{id} - PATCH
   * /api/v1/songs/{id} - DELETE /api/v1/songs/{id}
   *
   * @author Miguel David Palencia
   */

  private SongService songService;

  public SongController(SongService songService) {
    this.songService = songService;
  }

  @Operation(summary = "Crear canción")
  @PostMapping
  public ResponseEntity<SongResponse> create(@Valid @RequestBody SongCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(songService.create(request));
  }

  @Operation(summary = "Obtener canción por ID")
  @GetMapping("/{id}")
  public SongResponse getById(@PathVariable Long id) {
    return songService.findById(id);
  }

  @Operation(summary = "Listar/Buscar canciones",
      description = "Paginar y filtrar por título o artista. Si no hay filtros, lista todo."

  )
  @GetMapping
  public Page<SongResponse> list(
      @Parameter(description = "Filtro por título (contiene, ignore case)") @RequestParam(
          required = false) String title,
      @Parameter(description = "Filtro por artista (contiene, ignore case)") @RequestParam(
          required = false) String artist,
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
      @Parameter(description = "Campo de orden: title, artist, createdAt, etc.") @RequestParam(
          required = false) String sort) {
    return songService.search(title, artist, page, size, sort);
  }

  @Operation(summary = "Actualizar parcialmente una canción")
  @PatchMapping("/{id}")
  public SongResponse update(@PathVariable Long id, @Valid @RequestBody SongUpdateRequest request) {
    return songService.update(id, request);
  }

  @Operation(summary = "Eliminar canción")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    songService.delete(id);
    return ResponseEntity.noContent().build();
  }

}


