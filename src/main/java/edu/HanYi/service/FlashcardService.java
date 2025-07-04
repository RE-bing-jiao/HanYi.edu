package edu.HanYi.service;

import edu.HanYi.dto.request.FlashcardCreateRequest;
import edu.HanYi.dto.response.FlashcardResponse;

import java.util.List;

public interface FlashcardService {
    FlashcardResponse createFlashcard(FlashcardCreateRequest request);
    List<FlashcardResponse> getAllFlashcards();
    FlashcardResponse getFlashcardById(Integer id);
    List<FlashcardResponse> getFlashcardsByUserId(Integer userId);
    FlashcardResponse updateFlashcard(Integer id, FlashcardCreateRequest request);
    void deleteFlashcard(Integer id);
}