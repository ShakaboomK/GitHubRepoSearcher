package com.KKStands.GitHubExtends.GitHubRepoKK.DTO;

import lombok.Data;

import java.util.List;

@Data
public class GHubAPIResponseDTO {
    //maps with the API response Object of GitHub API
    private Integer totalCount;
    private Boolean incompleteResults;
    private List<GHubRepoDTO> items;

}
