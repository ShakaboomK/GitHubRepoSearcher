package com.KKStands.GitHubExtends.GitHubRepoKK.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Table(name = "git_hub_repositories")
@Data
public class GHubRepoEntity {
    @Id
    private Long id;

    private String name;

    @Column(length = 1024)
    private String description;

    private String owner;

    private String language;
    private Integer stars;
    private Integer forks;

    private ZonedDateTime lastUpdated;
}
