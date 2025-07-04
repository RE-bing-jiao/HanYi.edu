package edu.HanYi.service;

import edu.HanYi.dto.request.EntryCreateRequest;
import edu.HanYi.dto.response.EntryResponse;

import java.util.List;

public interface EntryService {
    EntryResponse createEntry(EntryCreateRequest request);
    List<EntryResponse> getAllEntries();
    EntryResponse getEntryById(Integer id);
    List<EntryResponse> getEntriesByUserId(Integer userId);
    List<EntryResponse> getEntriesByCourseId(Integer courseId);
    void deleteEntry(Integer id);
}