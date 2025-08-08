package edu.HanYi.controller;

import edu.HanYi.constants.LoggingConstants;
import edu.HanYi.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @PostMapping("/submit-contact")
    public String handleContactForm(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String message,
            RedirectAttributes redirectAttributes) {

        try {
            contactService.saveContactRequest(name, email, message);
            redirectAttributes.addFlashAttribute("success", "Ваша заявка успешно отправлена!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("name", name);
            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Произошла ошибка при отправке заявки");
            log.error(LoggingConstants.CONTACT_ERROR, e);
        }

        return "redirect:/home#contact";
    }
}
