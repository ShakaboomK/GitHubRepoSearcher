package com.KKStands.GitHubExtends.GitHubRepoKK.Service;

import com.KKStands.GitHubExtends.GitHubRepoKK.CustomExceptions.GHubApiException;
import com.KKStands.GitHubExtends.GitHubRepoKK.CustomExceptions.GHubRateLimitException;
import com.KKStands.GitHubExtends.GitHubRepoKK.DTO.GHubAPIResponseDTO;
import com.KKStands.GitHubExtends.GitHubRepoKK.DTO.GHubSearchRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class GHubAPIClientService {
    @Value("${my.app.github.token}")
    private String gitHubToken;
    private final WebClient webClient;

    public GHubAPIResponseDTO searchRepositories(GHubSearchRequestDTO request) {
        StringBuilder query = new StringBuilder(request.getQuery());

        if (request.getLanguage() != null && !request.getLanguage().isBlank()) {
            query.append("+language:").append(request.getLanguage());
        }

        String sort = (request.getSort() != null) ? request.getSort().name().toLowerCase() : "stars";

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/search/repositories")
                        .queryParam("q", query.toString())             // corrected param name 'q'
                        .queryParam("sort", sort)
                        .build())
                .header("Authorization", "token " + gitHubToken)
                .header("Accept", "application/vnd.github.v3+json")
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(GHubAPIResponseDTO.class);
                    } else if (response.statusCode().value() == 429 || response.statusCode().value() == 403) {
                        // GitHub rate limit hit
                        String resetTime = response.headers().header("X-RateLimit-Reset").stream().findFirst().orElse("unknown");
                        return Mono.error(new GHubRateLimitException("GitHub API rate limit exceeded. Retry after: " + resetTime));
                    } else {
                        // Other errors
                        return response.bodyToMono(String.class)
                                .defaultIfEmpty("Unknown error")
                                .flatMap(errorBody -> Mono.error(new GHubApiException("GitHub API error: " + errorBody)));
                    }
                })
                .block();
    }
}
