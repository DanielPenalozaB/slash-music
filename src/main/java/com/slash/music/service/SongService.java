package com.slash.music.service;

import com.slash.music.dto.*;
import org.springframework.data.domain.Page;

public interface SongService {
SongResponse create(SongCreateRequest request);
  SongResponse findById(Long id);
  Page<SongResponse> search(String title, String artist, int page, int size, String sort);
  SongResponse update(Long id, SongUpdateRequest request);
  void delete(Long id);
}
