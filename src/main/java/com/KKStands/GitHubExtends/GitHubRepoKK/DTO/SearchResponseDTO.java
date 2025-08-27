package com.KKStands.GitHubExtends.GitHubRepoKK.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResponseDTO {
    //Response DTO for POST /api/github/search endpoint.
    private String message;
    private List<RepositoryResponseDTO> repositories;
}
