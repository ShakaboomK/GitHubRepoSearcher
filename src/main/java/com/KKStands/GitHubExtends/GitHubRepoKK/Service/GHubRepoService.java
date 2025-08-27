package com.KKStands.GitHubExtends.GitHubRepoKK.Service;

import com.KKStands.GitHubExtends.GitHubRepoKK.DTO.*;
import com.KKStands.GitHubExtends.GitHubRepoKK.Entity.GHubRepoEntity;
import com.KKStands.GitHubExtends.GitHubRepoKK.Repository.GHubRepoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GHubRepoService {
    private final GHubRepoRepository  repository;

    private final GHubAPIClientService clientService;

    public SearchResponseDTO searchAndSaveRepositories(GHubSearchRequestDTO requestDTO){
        GHubAPIResponseDTO response = clientService.searchRepositories(requestDTO);
        if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
            return new SearchResponseDTO("NO Repositories are found", List.of());
        }
        List<GHubRepoEntity> entities = response.getItems().stream()
                .map(this::mapDTOtoEntity)
                .collect(Collectors.toList());
        repository.saveAll(entities);
        List<RepositoryResponseDTO>  responseDTOS =  entities.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
        return new SearchResponseDTO("Repositories fetched and Saved 200",responseDTOS );
    }

    public GetRepositoriesResponseDTO getRepositories(String language, Integer minStars, String sortBy) {
        // Default sort
        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "stars";
        }

        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort;
        switch (sortBy.toLowerCase()) {
            case "forks":
                sort = Sort.by(direction, "forks");
                break;
            case "updated":
                sort = Sort.by(direction, "lastUpdated");
                break;
            case "stars":
            default:
                sort = Sort.by(direction, "stars");
                break;
        }

        // Retrieve and filter results by criteria
        String finalSortBy = sortBy;
        List<GHubRepoEntity> entities = repository.findAll().stream()
                .filter(repo -> (language == null || language.isBlank() || language.equalsIgnoreCase(repo.getLanguage())))
                .filter(repo -> (minStars == null || repo.getStars() >= minStars))
                .sorted((r1, r2) -> {
                    switch (finalSortBy.toLowerCase()) {
                        case "forks":
                            return r2.getForks().compareTo(r1.getForks());
                        case "updated":
                            return r2.getLastUpdated().compareTo(r1.getLastUpdated());
                        case "stars":
                        default:
                            return r2.getStars().compareTo(r1.getStars());
                    }
                })
                .collect(Collectors.toList());

        List<RepositoryResponseDTO> resultDtos = entities.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

        return new GetRepositoriesResponseDTO(resultDtos);
    }
    private GHubRepoEntity mapDTOtoEntity(GHubRepoDTO dto) {
        GHubRepoEntity entity = new GHubRepoEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setOwner(dto.getOwner().getLogin());
        entity.setLanguage(dto.getLanguage());
        entity.setStars(dto.getStars());
        entity.setForks(dto.getForks());
        // Parse ISO date string into ZonedDateTime
        entity.setLastUpdated(ZonedDateTime.parse(dto.getUpdatedAt()));
        return entity;
    }

    private RepositoryResponseDTO mapEntityToDto(GHubRepoEntity entity) {
        RepositoryResponseDTO dto = new RepositoryResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setOwner(entity.getOwner());
        dto.setLanguage(entity.getLanguage());
        dto.setStars(entity.getStars());
        dto.setForks(entity.getForks());
        dto.setLastUpdated(entity.getLastUpdated());
        return dto;
    }

}
