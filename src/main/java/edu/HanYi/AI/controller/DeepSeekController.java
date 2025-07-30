package edu.HanYi.AI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.HanYi.AI.dto.DeepSeekResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class DeepSeekController {
    private static final String DEEPSEEK_API_URL = "https://api.deepseek.com/chat/completions";

    @Value("${deepseek.api.key}")
    private String API_KEY;

    @GetMapping("/chat")
    public String showChatForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "chat";
    }

    @PostMapping("/chat")
    public String sendMessage(@RequestParam String userMessage, Model model) {
        String htmlResponse = null;
        try {
            String systemMessage = "Answer only on the topic of the Chinese language. Answer in the language in which the question was asked. Be polite and concise.";
            String requestBody = new ObjectMapper().writeValueAsString(Map.of(
                    "model", "deepseek-chat",
                    "messages", List.of(
                            Map.of("role", "system", "content", systemMessage),
                            Map.of("role", "user", "content", userMessage)
                    ),
                    "stream", false
            ));

            ResponseEntity<String> response = new RestTemplate().postForEntity(
                    DEEPSEEK_API_URL,
                    new HttpEntity<>(requestBody, createHeaders()),
                    String.class
            );

            DeepSeekResponse apiResponse = new ObjectMapper()
                    .readValue(response.getBody(), DeepSeekResponse.class);

            String markdownResponse = apiResponse.getCleanContent();
            htmlResponse = convertMarkdownToHtml(markdownResponse);

            model.addAttribute("response", htmlResponse);
            model.addAttribute("userMessage", userMessage);

        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
        }
        log.info("HTML Response: {}", htmlResponse);
        return "chat";
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);
        return headers;
    }

    private String convertMarkdownToHtml(String markdown) {
        if (markdown == null) return "";
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(parser.parse(markdown));
    }
}