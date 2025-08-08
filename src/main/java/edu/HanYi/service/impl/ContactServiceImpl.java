package edu.HanYi.service.impl;

import edu.HanYi.constants.LoggingConstants;
import edu.HanYi.model.ContactRequest;
import edu.HanYi.repository.ContactRequestRepository;
import edu.HanYi.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRequestRepository contactRequestRepository;

    @Override
    @Transactional
    public void saveContactRequest(String name, String email, String message) {
        if (name == null || name.isBlank()) {
            log.error(LoggingConstants.CONTACT_NAME_BLANK);
            throw new IllegalArgumentException("Имя обязательно для заполнения");
        }

        if (email == null || email.isBlank()) {
            log.error(LoggingConstants.CONTACT_EMAIL_BLANK);
            throw new IllegalArgumentException("Email обязателен для заполнения");
        }

        ContactRequest request = new ContactRequest();
        request.setName(name.trim());
        request.setEmail(email.trim());
        request.setMessage(message != null ? message.trim() : null);

        log.info(LoggingConstants.CONTACT_REQUEST_SAVED, email);
        contactRequestRepository.save(request);
    }
}
