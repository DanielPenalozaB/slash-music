package com.slash.music.service;

import com.slash.music.dto.*;
import org.springframework.data.domain.Page;

/**
 * Application service for managing Songs. Provides create/read/update/delete operations and basic
 * search with pagination.
 *
 * @author Miguel David Palencia
 */

public interface SongService {

  /**
   * Creates a new Song from the given request payload.
   * 
   * @param request validated request payload
   * @return the created Song projected as a response DTO
   */
  SongResponse create(SongCreateRequest request);

  /**
   * Retrieves a Song by its identifier.
   * 
   * @param id song identifier
   * @return song projected as a response DTO
   */
  SongResponse findById(Long id);

  /**
   * Searches songs with optional filters and pagination.
   * 
   * @param title optional title filter (contains, case-insensitive)
   * @param artist optional artist filter (contains, case-insensitive)
   * @param page zero-based page index
   * @param size page size
   * @param sort optional sort property (e.g. id, title, createdAt)
   * @return page of song response DTOs
   */
  Page<SongResponse> search(String title, String artist, int page, int size, String sort);

  /**
   * Applies a partial update to the given song.
   * 
   * @param id song identifier
   * @param request fields to update
   * @return updated song response
   */
  SongResponse update(Long id, SongUpdateRequest request);

  /**
   * Deletes a song by its identifier.
   * 
   * @param id song identifier
   */
  void delete(Long id);
}
