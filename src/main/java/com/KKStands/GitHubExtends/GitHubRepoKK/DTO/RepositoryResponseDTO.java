package com.KKStands.GitHubExtends.GitHubRepoKK.DTO;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class RepositoryResponseDTO {
    //DTO to represent repository details sent in API response to clients.
    private Long id;
    private String name;
    private String description;
    private String owner;
    private String language;
    private Integer stars;
    private Integer forks;
    private ZonedDateTime lastUpdated;
}
