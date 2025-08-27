package com.KKStands.GitHubExtends.GitHubRepoKK.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GHubRepoDTO {
    //Maps the repository items returned by GitHub API. It corresponds to each repository's data in the JSON from GitHub.
    //Internal mapping of GitHub API JSON response
    private Long id;
    private String name;
    private String description;

    @JsonProperty("language")
    private String language;

    @JsonProperty("stargazers_count")
    private Integer stars;

    @JsonProperty("forks_count")
    private Integer forks;

    @JsonProperty("updated_at")
    private String updatedAt;

    private Owner owner;

    @Data
    public static class Owner {
        private String login; // Owner's username
    }
}
