package com.example.emailtemplategenerator.client;

import com.example.emailtemplategenerator.config.AiProperties;
import com.example.emailtemplategenerator.exception.AiServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Component
public class GeminiClient {

    private static final Logger log = LoggerFactory.getLogger(GeminiClient.class);

    private final RestClient restClient;
    private final AiProperties props;

    public GeminiClient(AiProperties props) {
        this.props = props;
        this.restClient = RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .defaultHeader("x-goog-api-key", props.getApiKey())
                .build();
    }

    public String generate(String prompt) {
        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
        );

        long start = System.nanoTime();
        try {
            Map<?, ?> response = restClient.post()
                    .uri("/models/{model}:generateContent", props.getModel())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(Map.class);
            return extractText(response);
        } catch (RestClientException e) {
            log.error("Gemini call failed: {}", e.getMessage());
            throw new AiServiceException("AI service call failed", e);
        } finally {
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            log.info("Gemini call finished in {} ms (model={})", elapsedMs, props.getModel());
        }
    }

    private String extractText(Map<?, ?> response) {
        try {
            List<?> candidates = (List<?>) response.get("candidates");
            Map<?, ?> content = (Map<?, ?>) ((Map<?, ?>) candidates.get(0)).get("content");
            List<?> parts = (List<?>) content.get("parts");
            return (String) ((Map<?, ?>) parts.get(0)).get("text");
        } catch (RuntimeException e) {
            throw new AiServiceException("Unexpected response from AI service", e);
        }
    }
}