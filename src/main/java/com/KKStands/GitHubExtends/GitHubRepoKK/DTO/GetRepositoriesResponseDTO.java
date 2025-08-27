package com.KKStands.GitHubExtends.GitHubRepoKK.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetRepositoriesResponseDTO {
    //Response wrapper for get stored repositories
    //Response DTO for GET /api/github/repositories endpoint.
    private List<RepositoryResponseDTO> repositories;
}
