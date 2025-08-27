package com.KKStands.GitHubExtends.GitHubRepoKK.Service;

import com.KKStands.GitHubExtends.GitHubRepoKK.DTO.GHubAPIResponseDTO;
import com.KKStands.GitHubExtends.GitHubRepoKK.DTO.GHubSearchRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class GHubAPIClientService {
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.github.com")
            .build();

    public GHubAPIResponseDTO searchRepositories(GHubSearchRequestDTO request) {
        StringBuilder query = new StringBuilder(request.getQuery());
        if (request.getLanguage() != null && !request.getLanguage().isBlank()){
            query.append("+language:").append(request.getLanguage());
        }
        String sort = (request.getSort() !=null)? request.getSort() : "stars";
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/search/repositories")
                        .queryParam("query", query.toString())
                        .queryParam("sort",sort)
                        .build())
                .retrieve()
                .bodyToMono(GHubAPIResponseDTO.class)
                .block();
    }
}
