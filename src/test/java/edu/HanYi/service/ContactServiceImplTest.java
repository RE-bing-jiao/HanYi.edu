package edu.HanYi.service;

import edu.HanYi.repository.ContactRequestRepository;
import edu.HanYi.service.impl.ContactServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @Mock
    private ContactRequestRepository contactRequestRepository;

    @InjectMocks
    private ContactServiceImpl contactService;

    @Test
    void saveContactRequest_ValidInput_SavesSuccessfully() {
        String name = "name";
        String email = "test@mail.ru";
        String message = "msg";
        contactService.saveContactRequest(name, email, message);

        verify(contactRequestRepository).save(any());
    }

    @Test
    void saveContactRequest_BlankName_ThrowsException() {
        String blankName = " ";
        String email = "test@mail.ru";

        assertThrows(IllegalArgumentException.class,
                () -> contactService.saveContactRequest(blankName, email, null));
    }

    @Test
    void saveContactRequest_BlankEmail_ThrowsException() {
        String name = "Test";
        String blankEmail = " ";

        assertThrows(IllegalArgumentException.class,
                () -> contactService.saveContactRequest(name, blankEmail, null));
    }
}
